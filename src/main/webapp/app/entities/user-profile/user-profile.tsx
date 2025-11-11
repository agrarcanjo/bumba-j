import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat, Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './user-profile.reducer';

export const UserProfile = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const userProfileList = useAppSelector(state => state.userProfile.entities);
  const loading = useAppSelector(state => state.userProfile.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="user-profile-heading" data-cy="UserProfileHeading">
        <Translate contentKey="bumbaApp.userProfile.home.title">User Profiles</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="bumbaApp.userProfile.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/user-profile/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="bumbaApp.userProfile.home.createLabel">Create new User Profile</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {userProfileList && userProfileList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="bumbaApp.userProfile.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('municipalityCode')}>
                  <Translate contentKey="bumbaApp.userProfile.municipalityCode">Municipality Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('municipalityCode')} />
                </th>
                <th className="hand" onClick={sort('currentLevel')}>
                  <Translate contentKey="bumbaApp.userProfile.currentLevel">Current Level</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('currentLevel')} />
                </th>
                <th className="hand" onClick={sort('totalXp')}>
                  <Translate contentKey="bumbaApp.userProfile.totalXp">Total Xp</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalXp')} />
                </th>
                <th className="hand" onClick={sort('currentStreak')}>
                  <Translate contentKey="bumbaApp.userProfile.currentStreak">Current Streak</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('currentStreak')} />
                </th>
                <th className="hand" onClick={sort('dailyGoalXp')}>
                  <Translate contentKey="bumbaApp.userProfile.dailyGoalXp">Daily Goal Xp</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dailyGoalXp')} />
                </th>
                <th className="hand" onClick={sort('lastActivityDate')}>
                  <Translate contentKey="bumbaApp.userProfile.lastActivityDate">Last Activity Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastActivityDate')} />
                </th>
                <th>
                  <Translate contentKey="bumbaApp.userProfile.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userProfileList.map((userProfile, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/user-profile/${userProfile.id}`} color="link" size="sm">
                      {userProfile.id}
                    </Button>
                  </td>
                  <td>{userProfile.municipalityCode}</td>
                  <td>
                    <Translate contentKey={`bumbaApp.UserLevel.${userProfile.currentLevel}`} />
                  </td>
                  <td>{userProfile.totalXp}</td>
                  <td>{userProfile.currentStreak}</td>
                  <td>{userProfile.dailyGoalXp}</td>
                  <td>
                    {userProfile.lastActivityDate ? (
                      <TextFormat type="date" value={userProfile.lastActivityDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{userProfile.user ? userProfile.user.id : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/user-profile/${userProfile.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/user-profile/${userProfile.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/user-profile/${userProfile.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="bumbaApp.userProfile.home.notFound">No User Profiles found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default UserProfile;
