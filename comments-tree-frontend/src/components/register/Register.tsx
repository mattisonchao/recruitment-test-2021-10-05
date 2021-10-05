import React from 'react';
import {FunctionComponent} from 'react';
import './register.scss';
import {Button, Form, Input, notification} from 'antd';
import {SmileOutlined, LockOutlined, MailOutlined, UserOutlined} from '@ant-design/icons';
import {useHistory} from 'react-router-dom';
import http from '../axios';


const Register: FunctionComponent = () => {
    const history = useHistory();
    const onFinish = (formData: any) => {
        http.post('/api/v1/auth/register', formData)
            .then((response) => {
                notification.open({
                    message: '温馨提醒',
                    description: '注册成功,自动跳转登录界面。',
                    icon: <SmileOutlined style={{color: '#108ee9'}}/>,
                });
                setTimeout(() => {
                    window.location.href = '/auth';
                }, 300);
            }).catch(() => {

        });
    };
    const goLogin = () => {
        history.push('/auth');
    };
    return <div className={'comments-tree-register-wrapper'}>
        <Form
            name="normal_login"
            className="comments-tree-register-form"
            initialValues={{remember: true}}
            onFinish={onFinish}
        >
            <Form.Item
                name="username"
                rules={[{required: true, pattern: /^[0-9A-Za-z]{5,20}$/i, message: '不可为空，只能使用字母和数字，长度在5~20之间'}]}
            >
                <Input prefix={<UserOutlined className="site-form-item-icon"/>} placeholder="用户名"/>
            </Form.Item>
            <Form.Item
                name="password"
                rules={[{
                    required: true,
                    pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,20}/,
                    message: '不可为空，长度在8~20之间，至少包含一个大写、一个小写、一个数字、一个特殊符号'
                }]}
            >
                <Input
                    prefix={<LockOutlined className="site-form-item-icon"/>}
                    type="password"
                    placeholder="密码"
                />
            </Form.Item>
            <Form.Item
                name="email"
                rules={[{required: true, message: '请输入正确的邮箱格式', type: 'email'}]}
            >
                <Input prefix={<MailOutlined className="site-form-item-icon"/>} placeholder="邮箱"/>
            </Form.Item>
            <Form.Item>
                <Button type="primary" block={true} htmlType="submit" className="login-form-button">
                    注册
                </Button>
                <Button onClick={goLogin} block={true} htmlType="button"
                        className="login-form-button comments-tree-register-login-button">
                    登录
                </Button>
            </Form.Item>
        </Form>
    </div>;
};
export default Register;