import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row, UncontrolledTooltip } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getLessons } from 'app/entities/lesson/lesson.reducer';
import { getEntities as getQuestions } from 'app/entities/question/question.reducer';
import { createEntity, getEntity, reset, updateEntity } from './lesson-question.reducer';

export const LessonQuestionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const lessons = useAppSelector(state => state.lesson.entities);
  const questions = useAppSelector(state => state.question.entities);
  const lessonQuestionEntity = useAppSelector(state => state.lessonQuestion.entity);
  const loading = useAppSelector(state => state.lessonQuestion.loading);
  const updating = useAppSelector(state => state.lessonQuestion.updating);
  const updateSuccess = useAppSelector(state => state.lessonQuestion.updateSuccess);

  const handleClose = () => {
    navigate('/lesson-question');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getLessons({}));
    dispatch(getQuestions({}));
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
    if (values.orderIndex !== undefined && typeof values.orderIndex !== 'number') {
      values.orderIndex = Number(values.orderIndex);
    }

    const entity = {
      ...lessonQuestionEntity,
      ...values,
      lesson: lessons.find(it => it.id.toString() === values.lesson?.toString()),
      question: questions.find(it => it.id.toString() === values.question?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...lessonQuestionEntity,
          lesson: lessonQuestionEntity?.lesson?.id,
          question: lessonQuestionEntity?.question?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bumbaApp.lessonQuestion.home.createOrEditLabel" data-cy="LessonQuestionCreateUpdateHeading">
            <Translate contentKey="bumbaApp.lessonQuestion.home.createOrEditLabel">Create or edit a LessonQuestion</Translate>
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
                  id="lesson-question-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bumbaApp.lessonQuestion.orderIndex')}
                id="lesson-question-orderIndex"
                name="orderIndex"
                data-cy="orderIndex"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <UncontrolledTooltip target="orderIndexLabel">
                <Translate contentKey="bumbaApp.lessonQuestion.help.orderIndex" />
              </UncontrolledTooltip>
              <ValidatedField
                id="lesson-question-lesson"
                name="lesson"
                data-cy="lesson"
                label={translate('bumbaApp.lessonQuestion.lesson')}
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
                id="lesson-question-question"
                name="question"
                data-cy="question"
                label={translate('bumbaApp.lessonQuestion.question')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/lesson-question" replace color="info">
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

export default LessonQuestionUpdate;
