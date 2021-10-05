import React from 'react';
import {FunctionComponent} from 'react';
import CommentsEditor from '../editor/CommentsEditor';
import './conmentcontent.scss';

interface Props {
    id: number;
    content: string;
    currentReply: number;
    onReplySubmit: (content:string) => void;
}

const CommentsContent: FunctionComponent<Props> = (props) => {
    return <div>
        {props.content}
        <div
            className={`comments-tree-reply-edit-wrapper ${props.id === props.currentReply ? '' : 'comments-tree-reply-edit-wrapper-hidden'}`}>
            <CommentsEditor minLength={2} onSubmit={props.onReplySubmit} loading={false}/>
        </div>
    </div>;
};
export default CommentsContent;