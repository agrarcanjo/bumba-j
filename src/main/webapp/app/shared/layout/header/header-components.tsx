import React from 'react';
import { Translate } from 'react-jhipster';

import { NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppSelector } from 'app/config/store';
import { AUTHORITIES } from 'app/config/constants';

export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <img src="content/images/logo.png" alt="Logo" />
  </div>
);

export const Brand = () => (
  <NavbarBrand tag={Link} to="/" className="brand-logo">
    <BrandIcon />
    <span className="brand-title">
      <Translate contentKey="global.title">Bumba</Translate>
    </span>
    <span className="navbar-version">{VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`}</span>
  </NavbarBrand>
);

export const Home = () => (
  <NavItem>
    <NavLink tag={Link} to="/" className="d-flex align-items-center">
      <FontAwesomeIcon icon="home" />
      <span>
        <Translate contentKey="global.menu.home">Home</Translate>
      </span>
    </NavLink>
  </NavItem>
);

export const DashboardLink = () => {
  const account = useAppSelector(state => state.authentication.account);
  const isStudent = account?.authorities?.includes(AUTHORITIES.ROLE_STUDENT);
  const isTeacher = account?.authorities?.includes(AUTHORITIES.ROLE_TEACHER);
  const isAdmin = account?.authorities?.includes(AUTHORITIES.ADMIN);

  let dashboardPath = '/';
  let dashboardLabel = 'Dashboard';

  if (isStudent) {
    dashboardPath = '/student/dashboard';
    dashboardLabel = 'Meu Dashboard';
  } else if (isTeacher) {
    dashboardPath = '/teacher/dashboard';
    dashboardLabel = 'Dashboard Professor';
  } else if (isAdmin) {
    dashboardPath = '/admin/dashboard';
    dashboardLabel = 'Dashboard Admin';
  }

  if (!isStudent && !isTeacher && !isAdmin) {
    return null;
  }

  return (
    <NavItem>
      <NavLink tag={Link} to={dashboardPath} className="d-flex align-items-center">
        <FontAwesomeIcon icon="tachometer-alt" />
        <span>{dashboardLabel}</span>
      </NavLink>
    </NavItem>
  );
};
