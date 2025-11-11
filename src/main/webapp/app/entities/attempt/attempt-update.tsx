import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row, UncontrolledTooltip } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getQuestions } from 'app/entities/question/question.reducer';
import { getEntities as getLessons } from 'app/entities/lesson/lesson.reducer';
import { createEntity, getEntity, reset, updateEntity } from './attempt.reducer';

export const AttemptUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const questions = useAppSelector(state => state.question.entities);
  const lessons = useAppSelector(state => state.lesson.entities);
  const attemptEntity = useAppSelector(state => state.attempt.entity);
  const loading = useAppSelector(state => state.attempt.loading);
  const updating = useAppSelector(state => state.attempt.updating);
  const updateSuccess = useAppSelector(state => state.attempt.updateSuccess);

  const handleClose = () => {
    navigate(`/attempt${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getQuestions({}));
    dispatch(getLessons({}));
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
    if (values.timeSpentSeconds !== undefined && typeof values.timeSpentSeconds !== 'number') {
      values.timeSpentSeconds = Number(values.timeSpentSeconds);
    }
    values.attemptedAt = convertDateTimeToServer(values.attemptedAt);

    const entity = {
      ...attemptEntity,
      ...values,
      student: users.find(it => it.id.toString() === values.student?.toString()),
      question: questions.find(it => it.id.toString() === values.question?.toString()),
      lesson: lessons.find(it => it.id.toString() === values.lesson?.toString()),
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
          attemptedAt: displayDefaultDateTime(),
        }
      : {
          ...attemptEntity,
          attemptedAt: convertDateTimeFromServer(attemptEntity.attemptedAt),
          student: attemptEntity?.student?.id,
          question: attemptEntity?.question?.id,
          lesson: attemptEntity?.lesson?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bumbaApp.attempt.home.createOrEditLabel" data-cy="AttemptCreateUpdateHeading">
            <Translate contentKey="bumbaApp.attempt.home.createOrEditLabel">Create or edit a Attempt</Translate>
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
                  id="attempt-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bumbaApp.attempt.isCorrect')}
                id="attempt-isCorrect"
                name="isCorrect"
                data-cy="isCorrect"
                check
                type="checkbox"
              />
              <UncontrolledTooltip target="isCorrectLabel">
                <Translate contentKey="bumbaApp.attempt.help.isCorrect" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.attempt.timeSpentSeconds')}
                id="attempt-timeSpentSeconds"
                name="timeSpentSeconds"
                data-cy="timeSpentSeconds"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <UncontrolledTooltip target="timeSpentSecondsLabel">
                <Translate contentKey="bumbaApp.attempt.help.timeSpentSeconds" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.attempt.attemptedAt')}
                id="attempt-attemptedAt"
                name="attemptedAt"
                data-cy="attemptedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <UncontrolledTooltip target="attemptedAtLabel">
                <Translate contentKey="bumbaApp.attempt.help.attemptedAt" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.attempt.answer')}
                id="attempt-answer"
                name="answer"
                data-cy="answer"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <UncontrolledTooltip target="answerLabel">
                <Translate contentKey="bumbaApp.attempt.help.answer" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.attempt.metadata')}
                id="attempt-metadata"
                name="metadata"
                data-cy="metadata"
                type="textarea"
              />
              <UncontrolledTooltip target="metadataLabel">
                <Translate contentKey="bumbaApp.attempt.help.metadata" />
              </UncontrolledTooltip>
              <ValidatedField
                id="attempt-student"
                name="student"
                data-cy="student"
                label={translate('bumbaApp.attempt.student')}
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
                id="attempt-question"
                name="question"
                data-cy="question"
                label={translate('bumbaApp.attempt.question')}
                type="select"
                required
              >
                <option value="" key="0" />
                {questions
                  ? questions.map(otherEntity => (
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
                id="attempt-lesson"
                name="lesson"
                data-cy="lesson"
                label={translate('bumbaApp.attempt.lesson')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/attempt" replace color="info">
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

export default AttemptUpdate;
