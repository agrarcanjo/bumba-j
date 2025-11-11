import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row, UncontrolledTooltip } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './student-lesson-progress.reducer';

export const StudentLessonProgressDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const studentLessonProgressEntity = useAppSelector(state => state.studentLessonProgress.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="studentLessonProgressDetailsHeading">
          <Translate contentKey="bumbaApp.studentLessonProgress.detail.title">StudentLessonProgress</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{studentLessonProgressEntity.id}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="bumbaApp.studentLessonProgress.status">Status</Translate>
            </span>
            <UncontrolledTooltip target="status">
              <Translate contentKey="bumbaApp.studentLessonProgress.help.status" />
            </UncontrolledTooltip>
          </dt>
          <dd>{studentLessonProgressEntity.status}</dd>
          <dt>
            <span id="score">
              <Translate contentKey="bumbaApp.studentLessonProgress.score">Score</Translate>
            </span>
            <UncontrolledTooltip target="score">
              <Translate contentKey="bumbaApp.studentLessonProgress.help.score" />
            </UncontrolledTooltip>
          </dt>
          <dd>{studentLessonProgressEntity.score}</dd>
          <dt>
            <span id="xpEarned">
              <Translate contentKey="bumbaApp.studentLessonProgress.xpEarned">Xp Earned</Translate>
            </span>
            <UncontrolledTooltip target="xpEarned">
              <Translate contentKey="bumbaApp.studentLessonProgress.help.xpEarned" />
            </UncontrolledTooltip>
          </dt>
          <dd>{studentLessonProgressEntity.xpEarned}</dd>
          <dt>
            <span id="completedAt">
              <Translate contentKey="bumbaApp.studentLessonProgress.completedAt">Completed At</Translate>
            </span>
            <UncontrolledTooltip target="completedAt">
              <Translate contentKey="bumbaApp.studentLessonProgress.help.completedAt" />
            </UncontrolledTooltip>
          </dt>
          <dd>
            {studentLessonProgressEntity.completedAt ? (
              <TextFormat value={studentLessonProgressEntity.completedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="isLate">
              <Translate contentKey="bumbaApp.studentLessonProgress.isLate">Is Late</Translate>
            </span>
            <UncontrolledTooltip target="isLate">
              <Translate contentKey="bumbaApp.studentLessonProgress.help.isLate" />
            </UncontrolledTooltip>
          </dt>
          <dd>{studentLessonProgressEntity.isLate ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="bumbaApp.studentLessonProgress.student">Student</Translate>
          </dt>
          <dd>{studentLessonProgressEntity.student ? studentLessonProgressEntity.student.id : ''}</dd>
          <dt>
            <Translate contentKey="bumbaApp.studentLessonProgress.lesson">Lesson</Translate>
          </dt>
          <dd>{studentLessonProgressEntity.lesson ? studentLessonProgressEntity.lesson.title : ''}</dd>
          <dt>
            <Translate contentKey="bumbaApp.studentLessonProgress.assignment">Assignment</Translate>
          </dt>
          <dd>{studentLessonProgressEntity.assignment ? studentLessonProgressEntity.assignment.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/student-lesson-progress" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/student-lesson-progress/${studentLessonProgressEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StudentLessonProgressDetail;
