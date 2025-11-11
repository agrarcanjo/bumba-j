import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row, UncontrolledTooltip } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './lesson-assignment.reducer';

export const LessonAssignmentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const lessonAssignmentEntity = useAppSelector(state => state.lessonAssignment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="lessonAssignmentDetailsHeading">
          <Translate contentKey="bumbaApp.lessonAssignment.detail.title">LessonAssignment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{lessonAssignmentEntity.id}</dd>
          <dt>
            <span id="assignedAt">
              <Translate contentKey="bumbaApp.lessonAssignment.assignedAt">Assigned At</Translate>
            </span>
            <UncontrolledTooltip target="assignedAt">
              <Translate contentKey="bumbaApp.lessonAssignment.help.assignedAt" />
            </UncontrolledTooltip>
          </dt>
          <dd>
            {lessonAssignmentEntity.assignedAt ? (
              <TextFormat value={lessonAssignmentEntity.assignedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="dueDate">
              <Translate contentKey="bumbaApp.lessonAssignment.dueDate">Due Date</Translate>
            </span>
            <UncontrolledTooltip target="dueDate">
              <Translate contentKey="bumbaApp.lessonAssignment.help.dueDate" />
            </UncontrolledTooltip>
          </dt>
          <dd>
            {lessonAssignmentEntity.dueDate ? (
              <TextFormat value={lessonAssignmentEntity.dueDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="bumbaApp.lessonAssignment.classRoom">Class Room</Translate>
          </dt>
          <dd>{lessonAssignmentEntity.classRoom ? lessonAssignmentEntity.classRoom.name : ''}</dd>
          <dt>
            <Translate contentKey="bumbaApp.lessonAssignment.lesson">Lesson</Translate>
          </dt>
          <dd>{lessonAssignmentEntity.lesson ? lessonAssignmentEntity.lesson.title : ''}</dd>
          <dt>
            <Translate contentKey="bumbaApp.lessonAssignment.assignedBy">Assigned By</Translate>
          </dt>
          <dd>{lessonAssignmentEntity.assignedBy ? lessonAssignmentEntity.assignedBy.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/lesson-assignment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/lesson-assignment/${lessonAssignmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LessonAssignmentDetail;
