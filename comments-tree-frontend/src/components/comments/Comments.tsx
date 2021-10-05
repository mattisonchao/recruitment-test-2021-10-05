import React, {useEffect, useLayoutEffect, useState} from 'react';
import {FunctionComponent} from 'react';
import {Button, Comment, Empty, Layout, List, Modal, notification, Tooltip} from 'antd';
import {Content, Header} from 'antd/es/layout/layout';
import {EditOutlined, SmileOutlined} from '@ant-design/icons';
import './comments.scss';
import axios from 'axios';
import Sider from 'antd/es/layout/Sider';
import CommentsEditor from '../editor/CommentsEditor';
import http from '../axios';
import moment from 'moment';
import 'moment/locale/zh-cn'
import CommentContent from './CommentContent';
import ReplyButton from './ReplyButton';

interface User {
    id: number;
    nickname: string;
    username: string;
    avatar: string;
    email: string;
    createTime: string;
    updateTime: string;
}

interface Comments {
    id: number;
    userId: number;
    userName: string;
    replyTo: string;
    rootCommentsId: number;
    content: string;
    createTime: string;
    updateTime: string;
}

interface CommentsNode {
    comments: Comments;
    subComments: CommentsNode[];
}


const Comments: FunctionComponent = () => {
    const [user, setUser] = useState<User>();
    const [commentsNodes, setCommentsNodes] = useState<CommentsNode[]>();
    const [reloadData, setReloadData] = useState(true);
    const [newCommentsVisible, setNewCommentsVisible] = useState(false);
    const [currentReply, setCurrentReply] = useState(-1);

    useLayoutEffect(() => {
        axios.get('/api/v1/auth/status')
            .then(({data}) => {
                setUser(data);
            }).catch(() => {
        });
    }, []);

    useEffect(() => {
        if (reloadData) {
            axios.get('/api/v1/comments')
                .then(({data}) => {
                    console.log(data);
                    setReloadData(false);
                    setCommentsNodes(data);
                }).catch(() => {
            });
        }
    }, [reloadData]);

    const postNewPrimaryComment = (content: string) => {
        postComment(null, content);
    };
    const postReplyComment = (content: string) => {
        postComment(currentReply, content);
    };

    const postComment = (replyTo: number | null, content: string) => {
        http.post('/api/v1/comments', {userId: user?.id, content, replyTo})
            .then(() => {
                notification.open({
                    message: '温馨提醒',
                    description: '添加成功',
                    icon: <SmileOutlined style={{color: '#108ee9'}}/>,
                });
                setNewCommentsVisible(false);
                setReloadData(true);
                setCurrentReply(-1);
            }).catch(() => {
        });
    };

    const showModal = () => {
        if (!user?.id) {
            window.location.href = '/auth';
        }
        setNewCommentsVisible(true);
    };

    const reloadComments = (nodeList: CommentsNode[] | undefined) => {
        if (!nodeList || nodeList.length === 0) {
            return;
        }
        return <List
            key={`list-${nodeList[0].comments.id}`}
            className="comment-list"
            itemLayout="horizontal"
            dataSource={nodeList}
            renderItem={item => (
                <li>
                    <Comment
                        actions={[<ReplyButton key={`list-reply-button-${item.comments.id}`} id={item.comments.id}
                                               currentReply={currentReply} onClickId={(id) => setCurrentReply(id)}/>]}
                        author={item.comments.userName}
                        avatar={'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png'}
                        content={
                            <CommentContent
                                id={item.comments.id}
                                onReplySubmit={postReplyComment}
                                content={item.comments.content}
                                key={`comment-content-${item.comments.id}`}
                                currentReply={currentReply}/>}
                        datetime={<Tooltip
                            title={moment(item.comments.createTime).subtract(1, 'days').format('YYYY-MM-DD HH:mm:ss')}>
                            <span>{moment(item.comments.createTime).fromNow()}</span>
                        </Tooltip>}
                        children={reloadComments(item.subComments)}
                    />
                </li>
            )}
        />;
    };


    return <>
        <Layout>
            <Header className="comments-tree-header">
                <div className={'comments-tree-header-content-wrapper'}>
                    <span className={'comments-tree-header-username'}> {user?.username} </span>
                    <span className={'comments-tree-header-mail'}> {user?.email} </span>
                </div>
            </Header>
            <Layout>
                <Sider theme={'light'}/>
                <Content className={'comments-tree-content'}>
                    {commentsNodes && commentsNodes.length > 0 ? reloadComments(commentsNodes) : <Empty description={'没有留言'}/>}
                </Content>
                <Sider theme={'light'}/>
                <div className={'comments-tree-edit-button-wrapper'}>
                    <Tooltip title="点击发布新评论">
                        <Button size={'large'} className={'comments-tree-edit-button'}
                                onClick={showModal} type="primary" shape="circle"
                                icon={<EditOutlined className={'comments-tree-edit-icon'}/>}/>
                    </Tooltip>
                </div>
            </Layout>
        </Layout>
        <Modal
            visible={newCommentsVisible}
            onCancel={() => setNewCommentsVisible(false)}
            title="请输入您的新评论"
            footer={null}
        >
            <CommentsEditor minLength={3} onSubmit={postNewPrimaryComment} loading={false}/>
        </Modal>
    </>;
};

export default Comments;