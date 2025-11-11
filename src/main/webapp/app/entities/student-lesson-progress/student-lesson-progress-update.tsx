import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row, UncontrolledTooltip } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getLessons } from 'app/entities/lesson/lesson.reducer';
import { getEntities as getLessonAssignments } from 'app/entities/lesson-assignment/lesson-assignment.reducer';
import { LessonStatus } from 'app/shared/model/enumerations/lesson-status.model';
import { createEntity, getEntity, reset, updateEntity } from './student-lesson-progress.reducer';

export const StudentLessonProgressUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const lessons = useAppSelector(state => state.lesson.entities);
  const lessonAssignments = useAppSelector(state => state.lessonAssignment.entities);
  const studentLessonProgressEntity = useAppSelector(state => state.studentLessonProgress.entity);
  const loading = useAppSelector(state => state.studentLessonProgress.loading);
  const updating = useAppSelector(state => state.studentLessonProgress.updating);
  const updateSuccess = useAppSelector(state => state.studentLessonProgress.updateSuccess);
  const lessonStatusValues = Object.keys(LessonStatus);

  const handleClose = () => {
    navigate('/student-lesson-progress');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getLessons({}));
    dispatch(getLessonAssignments({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.score !== undefined && typeof values.score !== 'number') {
      values.score = Number(values.score);
    }
    if (values.xpEarned !== undefined && typeof values.xpEarned !== 'number') {
      values.xpEarned = Number(values.xpEarned);
    }
    values.completedAt = convertDateTimeToServer(values.completedAt);

    const entity = {
      ...studentLessonProgressEntity,
      ...values,
      student: users.find(it => it.id.toString() === values.student?.toString()),
      lesson: lessons.find(it => it.id.toString() === values.lesson?.toString()),
      assignment: lessonAssignments.find(it => it.id.toString() === values.assignment?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          completedAt: displayDefaultDateTime(),
        }
      : {
          status: 'NOT_STARTED',
          ...studentLessonProgressEntity,
          completedAt: convertDateTimeFromServer(studentLessonProgressEntity.completedAt),
          student: studentLessonProgressEntity?.student?.id,
          lesson: studentLessonProgressEntity?.lesson?.id,
          assignment: studentLessonProgressEntity?.assignment?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bumbaApp.studentLessonProgress.home.createOrEditLabel" data-cy="StudentLessonProgressCreateUpdateHeading">
            <Translate contentKey="bumbaApp.studentLessonProgress.home.createOrEditLabel">Create or edit a StudentLessonProgress</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="student-lesson-progress-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bumbaApp.studentLessonProgress.status')}
                id="student-lesson-progress-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {lessonStatusValues.map(lessonStatus => (
                  <option value={lessonStatus} key={lessonStatus}>
                    {translate(`bumbaApp.LessonStatus.${lessonStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <UncontrolledTooltip target="statusLabel">
                <Translate contentKey="bumbaApp.studentLessonProgress.help.status" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.studentLessonProgress.score')}
                id="student-lesson-progress-score"
                name="score"
                data-cy="score"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  max: { value: 100, message: translate('entity.validation.max', { max: 100 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <UncontrolledTooltip target="scoreLabel">
                <Translate contentKey="bumbaApp.studentLessonProgress.help.score" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.studentLessonProgress.xpEarned')}
                id="student-lesson-progress-xpEarned"
                name="xpEarned"
                data-cy="xpEarned"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <UncontrolledTooltip target="xpEarnedLabel">
                <Translate contentKey="bumbaApp.studentLessonProgress.help.xpEarned" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.studentLessonProgress.completedAt')}
                id="student-lesson-progress-completedAt"
                name="completedAt"
                data-cy="completedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <UncontrolledTooltip target="completedAtLabel">
                <Translate contentKey="bumbaApp.studentLessonProgress.help.completedAt" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.studentLessonProgress.isLate')}
                id="student-lesson-progress-isLate"
                name="isLate"
                data-cy="isLate"
                check
                type="checkbox"
              />
              <UncontrolledTooltip target="isLateLabel">
                <Translate contentKey="bumbaApp.studentLessonProgress.help.isLate" />
              </UncontrolledTooltip>
              <ValidatedField
                id="student-lesson-progress-student"
                name="student"
                data-cy="student"
                label={translate('bumbaApp.studentLessonProgress.student')}
                type="select"
                required
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="student-lesson-progress-lesson"
                name="lesson"
                data-cy="lesson"
                label={translate('bumbaApp.studentLessonProgress.lesson')}
                type="select"
                required
              >
                <option value="" key="0" />
                {lessons
                  ? lessons.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.title}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="student-lesson-progress-assignment"
                name="assignment"
                data-cy="assignment"
                label={translate('bumbaApp.studentLessonProgress.assignment')}
                type="select"
              >
                <option value="" key="0" />
                {lessonAssignments
                  ? lessonAssignments.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/student-lesson-progress" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default StudentLessonProgressUpdate;
