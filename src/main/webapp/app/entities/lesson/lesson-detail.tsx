import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row, UncontrolledTooltip } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './lesson.reducer';

export const LessonDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const lessonEntity = useAppSelector(state => state.lesson.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="lessonDetailsHeading">
          <Translate contentKey="bumbaApp.lesson.detail.title">Lesson</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{lessonEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="bumbaApp.lesson.title">Title</Translate>
            </span>
            <UncontrolledTooltip target="title">
              <Translate contentKey="bumbaApp.lesson.help.title" />
            </UncontrolledTooltip>
          </dt>
          <dd>{lessonEntity.title}</dd>
          <dt>
            <span id="language">
              <Translate contentKey="bumbaApp.lesson.language">Language</Translate>
            </span>
            <UncontrolledTooltip target="language">
              <Translate contentKey="bumbaApp.lesson.help.language" />
            </UncontrolledTooltip>
          </dt>
          <dd>{lessonEntity.language}</dd>
          <dt>
            <span id="level">
              <Translate contentKey="bumbaApp.lesson.level">Level</Translate>
            </span>
            <UncontrolledTooltip target="level">
              <Translate contentKey="bumbaApp.lesson.help.level" />
            </UncontrolledTooltip>
          </dt>
          <dd>{lessonEntity.level}</dd>
          <dt>
            <span id="xpReward">
              <Translate contentKey="bumbaApp.lesson.xpReward">Xp Reward</Translate>
            </span>
            <UncontrolledTooltip target="xpReward">
              <Translate contentKey="bumbaApp.lesson.help.xpReward" />
            </UncontrolledTooltip>
          </dt>
          <dd>{lessonEntity.xpReward}</dd>
          <dt>
            <span id="passThreshold">
              <Translate contentKey="bumbaApp.lesson.passThreshold">Pass Threshold</Translate>
            </span>
            <UncontrolledTooltip target="passThreshold">
              <Translate contentKey="bumbaApp.lesson.help.passThreshold" />
            </UncontrolledTooltip>
          </dt>
          <dd>{lessonEntity.passThreshold}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="bumbaApp.lesson.createdAt">Created At</Translate>
            </span>
            <UncontrolledTooltip target="createdAt">
              <Translate contentKey="bumbaApp.lesson.help.createdAt" />
            </UncontrolledTooltip>
          </dt>
          <dd>{lessonEntity.createdAt ? <TextFormat value={lessonEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="bumbaApp.lesson.description">Description</Translate>
            </span>
            <UncontrolledTooltip target="description">
              <Translate contentKey="bumbaApp.lesson.help.description" />
            </UncontrolledTooltip>
          </dt>
          <dd>{lessonEntity.description}</dd>
          <dt>
            <Translate contentKey="bumbaApp.lesson.topic">Topic</Translate>
          </dt>
          <dd>{lessonEntity.topic ? lessonEntity.topic.name : ''}</dd>
          <dt>
            <Translate contentKey="bumbaApp.lesson.createdBy">Created By</Translate>
          </dt>
          <dd>{lessonEntity.createdBy ? lessonEntity.createdBy.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/lesson" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/lesson/${lessonEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LessonDetail;
