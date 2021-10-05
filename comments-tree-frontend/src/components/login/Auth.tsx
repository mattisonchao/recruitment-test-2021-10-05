import React, {useLayoutEffect} from 'react';
import {FunctionComponent} from 'react';
import {Form, Input, Button, Checkbox, notification} from 'antd';
import {UserOutlined, LockOutlined, MailOutlined, SmileOutlined} from '@ant-design/icons';
import {Tabs} from 'antd';
import './auth.scss';
import http from '../axios';
import axios from 'axios';

const {TabPane} = Tabs;

const Auth: FunctionComponent = () => {
    const onLogin = (param: any) => {
        const username = param.username;
        // 简单做，如果日后多方式则建立枚举
        const loginType = username ? 'USERNAME' : 'EMAIL';
        http.post('/api/v1/auth/login', {
            loginType,
            principal: username ? username : param.email,
            credentials: param.password,
            rememberMe: param.remember
        }).then(() => {
            notification.open({
                message: '温馨提醒',
                description: '登录成功',
                icon: <SmileOutlined style={{color: '#108ee9'}}/>,
            });
            setTimeout(() => {
                window.location.href = '/';
            }, 300);
        }).catch(() => {
        });
    };


    useLayoutEffect(() => {
        axios.get('/api/v1/auth/status')
            .then(({data}) => {
                window.location.href = '/';
            }).catch(() => {
        });
    }, []);

    return <div className={'comments-tree-login-tab-wrapper'}><Tabs defaultActiveKey="1" centered={true}
                                                                    className={'comments-tree-login-tab'}>
        <TabPane tab="用户名密码" key="1" className={'comments-tree-login-tabPane'}>
            <Form
                name="normal_login"
                className="login-form"
                initialValues={{remember: true}}
                onFinish={onLogin}
            >
                <Form.Item
                    name="username"
                    rules={[{required: true, message: '请输入您的用户名'}]}
                >
                    <Input prefix={<UserOutlined className="site-form-item-icon"/>} placeholder="用户名"/>
                </Form.Item>
                <Form.Item
                    name="password"
                    rules={[{required: true, message: '请输入您的密码'}]}
                >
                    <Input
                        prefix={<LockOutlined className="site-form-item-icon"/>}
                        type="password"
                        placeholder="密码"
                    />
                </Form.Item>
                <Form.Item>
                    <Form.Item name="remember" valuePropName="checked" noStyle={true}>
                        <Checkbox>请记住我</Checkbox>
                    </Form.Item>
                </Form.Item>
                <Form.Item>
                    <Button type="primary" htmlType="submit" className="login-form-button">
                        登录
                    </Button>
                    或 <a href="/register">立即注册</a>
                </Form.Item>
            </Form>
        </TabPane>
        <TabPane tab="邮箱账号" key="2" className={'comments-tree-login-tabPane'}>
            <Form
                name="normal_login"
                className="login-form"
                initialValues={{remember: true}}
                onFinish={onLogin}
            >
                <Form.Item
                    name="email"
                    rules={[{required: true, message: '请输入正确的邮箱格式', type: 'email'}]}
                >
                    <Input prefix={<MailOutlined className="site-form-item-icon"/>} placeholder="邮箱"/>
                </Form.Item>
                <Form.Item
                    name="password"
                    rules={[{required: true, message: '请输入您的密码'}]}
                >
                    <Input
                        prefix={<LockOutlined className="site-form-item-icon"/>}
                        type="password"
                        placeholder="密码"
                    />
                </Form.Item>
                <Form.Item>
                    <Form.Item name="remember" valuePropName="checked" noStyle={true}>
                        <Checkbox>请记住我</Checkbox>
                    </Form.Item>
                </Form.Item>
                <Form.Item>
                    <Button type="primary" htmlType="submit" className="login-form-button">
                        登录
                    </Button>
                    或 <a href="/register">立即注册</a>
                </Form.Item>
            </Form>
        </TabPane>
    </Tabs></div>;
};
export default Auth;