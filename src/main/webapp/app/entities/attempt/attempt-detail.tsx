import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row, UncontrolledTooltip } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './attempt.reducer';

export const AttemptDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const attemptEntity = useAppSelector(state => state.attempt.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="attemptDetailsHeading">
          <Translate contentKey="bumbaApp.attempt.detail.title">Attempt</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{attemptEntity.id}</dd>
          <dt>
            <span id="isCorrect">
              <Translate contentKey="bumbaApp.attempt.isCorrect">Is Correct</Translate>
            </span>
            <UncontrolledTooltip target="isCorrect">
              <Translate contentKey="bumbaApp.attempt.help.isCorrect" />
            </UncontrolledTooltip>
          </dt>
          <dd>{attemptEntity.isCorrect ? 'true' : 'false'}</dd>
          <dt>
            <span id="timeSpentSeconds">
              <Translate contentKey="bumbaApp.attempt.timeSpentSeconds">Time Spent Seconds</Translate>
            </span>
            <UncontrolledTooltip target="timeSpentSeconds">
              <Translate contentKey="bumbaApp.attempt.help.timeSpentSeconds" />
            </UncontrolledTooltip>
          </dt>
          <dd>{attemptEntity.timeSpentSeconds}</dd>
          <dt>
            <span id="attemptedAt">
              <Translate contentKey="bumbaApp.attempt.attemptedAt">Attempted At</Translate>
            </span>
            <UncontrolledTooltip target="attemptedAt">
              <Translate contentKey="bumbaApp.attempt.help.attemptedAt" />
            </UncontrolledTooltip>
          </dt>
          <dd>
            {attemptEntity.attemptedAt ? <TextFormat value={attemptEntity.attemptedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="answer">
              <Translate contentKey="bumbaApp.attempt.answer">Answer</Translate>
            </span>
            <UncontrolledTooltip target="answer">
              <Translate contentKey="bumbaApp.attempt.help.answer" />
            </UncontrolledTooltip>
          </dt>
          <dd>{attemptEntity.answer}</dd>
          <dt>
            <span id="metadata">
              <Translate contentKey="bumbaApp.attempt.metadata">Metadata</Translate>
            </span>
            <UncontrolledTooltip target="metadata">
              <Translate contentKey="bumbaApp.attempt.help.metadata" />
            </UncontrolledTooltip>
          </dt>
          <dd>{attemptEntity.metadata}</dd>
          <dt>
            <Translate contentKey="bumbaApp.attempt.student">Student</Translate>
          </dt>
          <dd>{attemptEntity.student ? attemptEntity.student.id : ''}</dd>
          <dt>
            <Translate contentKey="bumbaApp.attempt.question">Question</Translate>
          </dt>
          <dd>{attemptEntity.question ? attemptEntity.question.id : ''}</dd>
          <dt>
            <Translate contentKey="bumbaApp.attempt.lesson">Lesson</Translate>
          </dt>
          <dd>{attemptEntity.lesson ? attemptEntity.lesson.title : ''}</dd>
        </dl>
        <Button tag={Link} to="/attempt" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/attempt/${attemptEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AttemptDetail;
