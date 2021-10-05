import React from 'react';
import {FunctionComponent} from 'react';
import ReactDom from 'react-dom';
import {BrowserRouter, Route, Switch} from 'react-router-dom';
import Auth from './components/login/Auth';
import Comments from './components/comments/Comments';
import './app.scss';
import 'antd/dist/antd.css';
import Register from './components/register/Register';


const App: FunctionComponent = () => {
    return (
        <BrowserRouter>
            <Switch>
                <Route exact={true} path={'/'} component={Comments}/>
                <Route path={'/auth'} component={Auth}/>
                <Route path={'/register'} component={Register}/>
            </Switch>
        </BrowserRouter>
    );
};

ReactDom.render(
    <App/>
    , document.querySelector('#root'));