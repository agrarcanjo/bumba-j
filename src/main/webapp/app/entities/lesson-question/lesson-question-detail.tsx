import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row, UncontrolledTooltip } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './lesson-question.reducer';

export const LessonQuestionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const lessonQuestionEntity = useAppSelector(state => state.lessonQuestion.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="lessonQuestionDetailsHeading">
          <Translate contentKey="bumbaApp.lessonQuestion.detail.title">LessonQuestion</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{lessonQuestionEntity.id}</dd>
          <dt>
            <span id="orderIndex">
              <Translate contentKey="bumbaApp.lessonQuestion.orderIndex">Order Index</Translate>
            </span>
            <UncontrolledTooltip target="orderIndex">
              <Translate contentKey="bumbaApp.lessonQuestion.help.orderIndex" />
            </UncontrolledTooltip>
          </dt>
          <dd>{lessonQuestionEntity.orderIndex}</dd>
          <dt>
            <Translate contentKey="bumbaApp.lessonQuestion.lesson">Lesson</Translate>
          </dt>
          <dd>{lessonQuestionEntity.lesson ? lessonQuestionEntity.lesson.title : ''}</dd>
          <dt>
            <Translate contentKey="bumbaApp.lessonQuestion.question">Question</Translate>
          </dt>
          <dd>{lessonQuestionEntity.question ? lessonQuestionEntity.question.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/lesson-question" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/lesson-question/${lessonQuestionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LessonQuestionDetail;
