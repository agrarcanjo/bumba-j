import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row, UncontrolledTooltip } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-achievement.reducer';

export const UserAchievementDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userAchievementEntity = useAppSelector(state => state.userAchievement.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userAchievementDetailsHeading">
          <Translate contentKey="bumbaApp.userAchievement.detail.title">UserAchievement</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userAchievementEntity.id}</dd>
          <dt>
            <span id="unlockedAt">
              <Translate contentKey="bumbaApp.userAchievement.unlockedAt">Unlocked At</Translate>
            </span>
            <UncontrolledTooltip target="unlockedAt">
              <Translate contentKey="bumbaApp.userAchievement.help.unlockedAt" />
            </UncontrolledTooltip>
          </dt>
          <dd>
            {userAchievementEntity.unlockedAt ? (
              <TextFormat value={userAchievementEntity.unlockedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="bumbaApp.userAchievement.user">User</Translate>
          </dt>
          <dd>{userAchievementEntity.user ? userAchievementEntity.user.id : ''}</dd>
          <dt>
            <Translate contentKey="bumbaApp.userAchievement.achievement">Achievement</Translate>
          </dt>
          <dd>{userAchievementEntity.achievement ? userAchievementEntity.achievement.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-achievement" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-achievement/${userAchievementEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserAchievementDetail;
