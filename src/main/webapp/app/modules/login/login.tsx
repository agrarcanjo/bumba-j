import React, { useEffect, useState } from 'react';
import { Navigate, useLocation, useNavigate } from 'react-router-dom';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { login } from 'app/shared/reducers/authentication';
import LoginModal from './login-modal';
import { AUTHORITIES } from 'app/config/constants';

export const Login = () => {
  const dispatch = useAppDispatch();
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const loginError = useAppSelector(state => state.authentication.loginError);
  const showModalLogin = useAppSelector(state => state.authentication.showModalLogin);
  const account = useAppSelector(state => state.authentication.account);
  const [showModal, setShowModal] = useState(showModalLogin);
  const navigate = useNavigate();
  const pageLocation = useLocation();

  useEffect(() => {
    setShowModal(true);
  }, []);

  const handleLogin = (username, password, rememberMe = false) => dispatch(login(username, password, rememberMe));

  const handleClose = () => {
    setShowModal(false);
    navigate('/');
  };

  const { from } = pageLocation.state || { from: { pathname: '/', search: pageLocation.search } };

  if (isAuthenticated) {
    if (account?.authorities?.includes(AUTHORITIES.ROLE_STUDENT)) {
      return <Navigate to="/student/dashboard" replace />;
    }
    if (account?.authorities?.includes(AUTHORITIES.ROLE_TEACHER)) {
      return <Navigate to="/teacher/dashboard" replace />;
    }
    if (account?.authorities?.includes(AUTHORITIES.ADMIN)) {
      return <Navigate to="/admin/dashboard" replace />;
    }
    return <Navigate to={from} replace />;
  }

  return <LoginModal showModal={showModal} handleLogin={handleLogin} handleClose={handleClose} loginError={loginError} />;
};

export default Login;
