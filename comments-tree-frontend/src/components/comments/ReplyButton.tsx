import React from 'react';
import {FunctionComponent} from 'react';
import './replyButton.scss';

interface Props {
    id: number;
    onClickId: (id: number) => void;
    currentReply: number;
}

const ReplyButton: FunctionComponent<Props> = (props) => {
    const onClick = () => {
        props.onClickId(props.id);
    };
    return <span className={props.currentReply === props.id ? 'comments-tree-reply-button' : ''}
                 onClick={onClick}>回复</span>;
};
export default ReplyButton;