import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row, UncontrolledTooltip } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getClassRooms } from 'app/entities/class-room/class-room.reducer';
import { getEntities as getLessons } from 'app/entities/lesson/lesson.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { createEntity, getEntity, reset, updateEntity } from './lesson-assignment.reducer';

export const LessonAssignmentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const classRooms = useAppSelector(state => state.classRoom.entities);
  const lessons = useAppSelector(state => state.lesson.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const lessonAssignmentEntity = useAppSelector(state => state.lessonAssignment.entity);
  const loading = useAppSelector(state => state.lessonAssignment.loading);
  const updating = useAppSelector(state => state.lessonAssignment.updating);
  const updateSuccess = useAppSelector(state => state.lessonAssignment.updateSuccess);

  const handleClose = () => {
    navigate('/lesson-assignment');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getClassRooms({}));
    dispatch(getLessons({}));
    dispatch(getUsers({}));
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
    values.assignedAt = convertDateTimeToServer(values.assignedAt);
    values.dueDate = convertDateTimeToServer(values.dueDate);

    const entity = {
      ...lessonAssignmentEntity,
      ...values,
      classRoom: classRooms.find(it => it.id.toString() === values.classRoom?.toString()),
      lesson: lessons.find(it => it.id.toString() === values.lesson?.toString()),
      assignedBy: users.find(it => it.id.toString() === values.assignedBy?.toString()),
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
          assignedAt: displayDefaultDateTime(),
          dueDate: displayDefaultDateTime(),
        }
      : {
          ...lessonAssignmentEntity,
          assignedAt: convertDateTimeFromServer(lessonAssignmentEntity.assignedAt),
          dueDate: convertDateTimeFromServer(lessonAssignmentEntity.dueDate),
          classRoom: lessonAssignmentEntity?.classRoom?.id,
          lesson: lessonAssignmentEntity?.lesson?.id,
          assignedBy: lessonAssignmentEntity?.assignedBy?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bumbaApp.lessonAssignment.home.createOrEditLabel" data-cy="LessonAssignmentCreateUpdateHeading">
            <Translate contentKey="bumbaApp.lessonAssignment.home.createOrEditLabel">Create or edit a LessonAssignment</Translate>
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
                  id="lesson-assignment-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bumbaApp.lessonAssignment.assignedAt')}
                id="lesson-assignment-assignedAt"
                name="assignedAt"
                data-cy="assignedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <UncontrolledTooltip target="assignedAtLabel">
                <Translate contentKey="bumbaApp.lessonAssignment.help.assignedAt" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.lessonAssignment.dueDate')}
                id="lesson-assignment-dueDate"
                name="dueDate"
                data-cy="dueDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <UncontrolledTooltip target="dueDateLabel">
                <Translate contentKey="bumbaApp.lessonAssignment.help.dueDate" />
              </UncontrolledTooltip>
              <ValidatedField
                id="lesson-assignment-classRoom"
                name="classRoom"
                data-cy="classRoom"
                label={translate('bumbaApp.lessonAssignment.classRoom')}
                type="select"
                required
              >
                <option value="" key="0" />
                {classRooms
                  ? classRooms.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="lesson-assignment-lesson"
                name="lesson"
                data-cy="lesson"
                label={translate('bumbaApp.lessonAssignment.lesson')}
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
                id="lesson-assignment-assignedBy"
                name="assignedBy"
                data-cy="assignedBy"
                label={translate('bumbaApp.lessonAssignment.assignedBy')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/lesson-assignment" replace color="info">
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

export default LessonAssignmentUpdate;
