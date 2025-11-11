import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row, UncontrolledTooltip } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './achievement.reducer';

export const AchievementDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const achievementEntity = useAppSelector(state => state.achievement.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="achievementDetailsHeading">
          <Translate contentKey="bumbaApp.achievement.detail.title">Achievement</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{achievementEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="bumbaApp.achievement.code">Code</Translate>
            </span>
            <UncontrolledTooltip target="code">
              <Translate contentKey="bumbaApp.achievement.help.code" />
            </UncontrolledTooltip>
          </dt>
          <dd>{achievementEntity.code}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="bumbaApp.achievement.name">Name</Translate>
            </span>
            <UncontrolledTooltip target="name">
              <Translate contentKey="bumbaApp.achievement.help.name" />
            </UncontrolledTooltip>
          </dt>
          <dd>{achievementEntity.name}</dd>
          <dt>
            <span id="iconUrl">
              <Translate contentKey="bumbaApp.achievement.iconUrl">Icon Url</Translate>
            </span>
            <UncontrolledTooltip target="iconUrl">
              <Translate contentKey="bumbaApp.achievement.help.iconUrl" />
            </UncontrolledTooltip>
          </dt>
          <dd>{achievementEntity.iconUrl}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="bumbaApp.achievement.description">Description</Translate>
            </span>
            <UncontrolledTooltip target="description">
              <Translate contentKey="bumbaApp.achievement.help.description" />
            </UncontrolledTooltip>
          </dt>
          <dd>{achievementEntity.description}</dd>
          <dt>
            <span id="criteria">
              <Translate contentKey="bumbaApp.achievement.criteria">Criteria</Translate>
            </span>
            <UncontrolledTooltip target="criteria">
              <Translate contentKey="bumbaApp.achievement.help.criteria" />
            </UncontrolledTooltip>
          </dt>
          <dd>{achievementEntity.criteria}</dd>
        </dl>
        <Button tag={Link} to="/achievement" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/achievement/${achievementEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AchievementDetail;
