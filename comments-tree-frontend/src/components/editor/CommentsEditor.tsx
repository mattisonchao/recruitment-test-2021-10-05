import {Button, Form} from 'antd';
import TextArea from 'antd/es/input/TextArea';
import React, {FunctionComponent, useState} from 'react';
import './commentsEditor.scss';

interface Props {
    onSubmit: (data: any) => void;
    loading: boolean;
    minLength: number;
}

const CommentsEditor: FunctionComponent<Props> = (props) => {
    const [content, setContent] = useState('');
    const [showError, setShowErrorr] = useState(false);
    const onChange = (event: any) => {
        setShowErrorr(content.length < props.minLength - 1);
        setContent(event.currentTarget.value);
    };
    const onClick = () => {
        if (content.length < props.minLength - 1) {
            setShowErrorr(true);
            return;
        }
        props.onSubmit(content);
        setContent('');
    };
    return <>
        <Form.Item>
            <TextArea showCount={true} allowClear={true} value={content} maxLength={200} rows={4}
                      onChange={onChange}/>
            <span className={`comments-tree-edit-error ${showError ? '' : 'hidden'}`}>{'留言长度请在3~200字之间'}</span>
        </Form.Item>
        <Form.Item>
            <Button htmlType="submit" loading={props.loading} onClick={onClick} type="primary">
                提交
            </Button>
        </Form.Item>
    </>;
};

export default CommentsEditor;