import './header.scss';

import React, { useState } from 'react';
import { Storage, Translate } from 'react-jhipster';
import { Collapse, Nav, Navbar, NavbarToggler } from 'reactstrap';
import LoadingBar from 'react-redux-loading-bar';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { setLocale } from 'app/shared/reducers/locale';
import { AccountMenu, AdminMenu } from '../menus';
import { Brand, Home, DashboardLink } from './header-components';
import HeaderStats from './HeaderStats';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBars } from '@fortawesome/free-solid-svg-icons';

export interface IHeaderProps {
  isAuthenticated: boolean;
  isAdmin: boolean;
  ribbonEnv: string;
  isInProduction: boolean;
  isOpenAPIEnabled: boolean;
  currentLocale: string;
  onToggleSidebar?: () => void;
}

const Header = (props: IHeaderProps) => {
  const [menuOpen, setMenuOpen] = useState(false);
  const account = useAppSelector(state => state.authentication.account);
  const isStudent = account?.authorities?.includes('ROLE_STUDENT');
  const isTeacher = account?.authorities?.includes('ROLE_TEACHER');
  const isAdmin = account?.authorities?.includes('ROLE_ADMIN');

  const dispatch = useAppDispatch();

  const handleLocaleChange = event => {
    const langKey = event.target.value;
    Storage.session.set('locale', langKey);
    dispatch(setLocale(langKey));
  };

  const renderDevRibbon = () =>
    props.isInProduction === false ? (
      <div className="ribbon dev">
        <a href="">
          <Translate contentKey={`global.ribbon.${props.ribbonEnv}`} />
        </a>
      </div>
    ) : null;

  const toggleMenu = () => setMenuOpen(!menuOpen);

  return (
    <div id="app-header">
      {renderDevRibbon()}
      <LoadingBar className="loading-bar" />
      <Navbar data-cy="navbar" dark expand="md" fixed="top" className="bg-primary">
        {(isStudent || isTeacher || isAdmin) && props.onToggleSidebar && (
          <button className="sidebar-toggle-btn" onClick={props.onToggleSidebar} aria-label="Toggle sidebar">
            <FontAwesomeIcon icon={faBars} />
          </button>
        )}
        <NavbarToggler aria-label="Menu" onClick={toggleMenu} />
        <Brand />
        <Collapse isOpen={menuOpen} navbar>
          <Nav id="header-tabs" className="ms-auto" navbar>
            <Home />
            <DashboardLink />
            {props.isAuthenticated && props.isAdmin && <AdminMenu showOpenAPI={props.isOpenAPIEnabled} />}
            {props.isAuthenticated && isStudent && <HeaderStats />}
            <AccountMenu isAuthenticated={props.isAuthenticated} />
          </Nav>
        </Collapse>
      </Navbar>
    </div>
  );
};

export default Header;
