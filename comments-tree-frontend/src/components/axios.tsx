import axios from 'axios';
import {notification} from 'antd';
import React from 'react';
import {FrownOutlined} from '@ant-design/icons';

const http = axios.create({
    baseURL: '/'
});

http.interceptors.response.use(
    response => {
        return response;
    },
    error => {
        const response = error.response;
        if (response.data){
            const statusCode = response.data.statusCode;
            if (statusCode === 401){
                window.location.href = '/auth';
            }else{
                notification.open({
                    message:' 异常提醒 ',
                    description: response.data.message,
                    icon: <FrownOutlined style={{color: '#108ee9'}}/>,
                });
            }
        }
        return Promise.reject(error);
    }
);

export default http;


