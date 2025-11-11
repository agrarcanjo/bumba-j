import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row, UncontrolledTooltip } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './topic.reducer';

export const TopicDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const topicEntity = useAppSelector(state => state.topic.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="topicDetailsHeading">
          <Translate contentKey="bumbaApp.topic.detail.title">Topic</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{topicEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="bumbaApp.topic.name">Name</Translate>
            </span>
            <UncontrolledTooltip target="name">
              <Translate contentKey="bumbaApp.topic.help.name" />
            </UncontrolledTooltip>
          </dt>
          <dd>{topicEntity.name}</dd>
          <dt>
            <span id="language">
              <Translate contentKey="bumbaApp.topic.language">Language</Translate>
            </span>
            <UncontrolledTooltip target="language">
              <Translate contentKey="bumbaApp.topic.help.language" />
            </UncontrolledTooltip>
          </dt>
          <dd>{topicEntity.language}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="bumbaApp.topic.description">Description</Translate>
            </span>
            <UncontrolledTooltip target="description">
              <Translate contentKey="bumbaApp.topic.help.description" />
            </UncontrolledTooltip>
          </dt>
          <dd>{topicEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/topic" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/topic/${topicEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TopicDetail;
