import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row, UncontrolledTooltip } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-profile.reducer';

export const UserProfileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userProfileEntity = useAppSelector(state => state.userProfile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userProfileDetailsHeading">
          <Translate contentKey="bumbaApp.userProfile.detail.title">UserProfile</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.id}</dd>
          <dt>
            <span id="municipalityCode">
              <Translate contentKey="bumbaApp.userProfile.municipalityCode">Municipality Code</Translate>
            </span>
            <UncontrolledTooltip target="municipalityCode">
              <Translate contentKey="bumbaApp.userProfile.help.municipalityCode" />
            </UncontrolledTooltip>
          </dt>
          <dd>{userProfileEntity.municipalityCode}</dd>
          <dt>
            <span id="currentLevel">
              <Translate contentKey="bumbaApp.userProfile.currentLevel">Current Level</Translate>
            </span>
            <UncontrolledTooltip target="currentLevel">
              <Translate contentKey="bumbaApp.userProfile.help.currentLevel" />
            </UncontrolledTooltip>
          </dt>
          <dd>{userProfileEntity.currentLevel}</dd>
          <dt>
            <span id="totalXp">
              <Translate contentKey="bumbaApp.userProfile.totalXp">Total Xp</Translate>
            </span>
            <UncontrolledTooltip target="totalXp">
              <Translate contentKey="bumbaApp.userProfile.help.totalXp" />
            </UncontrolledTooltip>
          </dt>
          <dd>{userProfileEntity.totalXp}</dd>
          <dt>
            <span id="currentStreak">
              <Translate contentKey="bumbaApp.userProfile.currentStreak">Current Streak</Translate>
            </span>
            <UncontrolledTooltip target="currentStreak">
              <Translate contentKey="bumbaApp.userProfile.help.currentStreak" />
            </UncontrolledTooltip>
          </dt>
          <dd>{userProfileEntity.currentStreak}</dd>
          <dt>
            <span id="dailyGoalXp">
              <Translate contentKey="bumbaApp.userProfile.dailyGoalXp">Daily Goal Xp</Translate>
            </span>
            <UncontrolledTooltip target="dailyGoalXp">
              <Translate contentKey="bumbaApp.userProfile.help.dailyGoalXp" />
            </UncontrolledTooltip>
          </dt>
          <dd>{userProfileEntity.dailyGoalXp}</dd>
          <dt>
            <span id="lastActivityDate">
              <Translate contentKey="bumbaApp.userProfile.lastActivityDate">Last Activity Date</Translate>
            </span>
            <UncontrolledTooltip target="lastActivityDate">
              <Translate contentKey="bumbaApp.userProfile.help.lastActivityDate" />
            </UncontrolledTooltip>
          </dt>
          <dd>
            {userProfileEntity.lastActivityDate ? (
              <TextFormat value={userProfileEntity.lastActivityDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="bumbaApp.userProfile.user">User</Translate>
          </dt>
          <dd>{userProfileEntity.user ? userProfileEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-profile" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-profile/${userProfileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserProfileDetail;
