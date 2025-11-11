import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row, UncontrolledTooltip } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getTopics } from 'app/entities/topic/topic.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { Language } from 'app/shared/model/enumerations/language.model';
import { DifficultyLevel } from 'app/shared/model/enumerations/difficulty-level.model';
import { createEntity, getEntity, reset, updateEntity } from './lesson.reducer';

export const LessonUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const topics = useAppSelector(state => state.topic.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const lessonEntity = useAppSelector(state => state.lesson.entity);
  const loading = useAppSelector(state => state.lesson.loading);
  const updating = useAppSelector(state => state.lesson.updating);
  const updateSuccess = useAppSelector(state => state.lesson.updateSuccess);
  const languageValues = Object.keys(Language);
  const difficultyLevelValues = Object.keys(DifficultyLevel);

  const handleClose = () => {
    navigate(`/lesson${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTopics({}));
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
    if (values.xpReward !== undefined && typeof values.xpReward !== 'number') {
      values.xpReward = Number(values.xpReward);
    }
    if (values.passThreshold !== undefined && typeof values.passThreshold !== 'number') {
      values.passThreshold = Number(values.passThreshold);
    }
    values.createdAt = convertDateTimeToServer(values.createdAt);

    const entity = {
      ...lessonEntity,
      ...values,
      topic: topics.find(it => it.id.toString() === values.topic?.toString()),
      createdBy: users.find(it => it.id.toString() === values.createdBy?.toString()),
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
          createdAt: displayDefaultDateTime(),
        }
      : {
          language: 'ENGLISH',
          level: 'EASY',
          ...lessonEntity,
          createdAt: convertDateTimeFromServer(lessonEntity.createdAt),
          topic: lessonEntity?.topic?.id,
          createdBy: lessonEntity?.createdBy?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bumbaApp.lesson.home.createOrEditLabel" data-cy="LessonCreateUpdateHeading">
            <Translate contentKey="bumbaApp.lesson.home.createOrEditLabel">Create or edit a Lesson</Translate>
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
                  id="lesson-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bumbaApp.lesson.title')}
                id="lesson-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 200, message: translate('entity.validation.maxlength', { max: 200 }) },
                }}
              />
              <UncontrolledTooltip target="titleLabel">
                <Translate contentKey="bumbaApp.lesson.help.title" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.lesson.language')}
                id="lesson-language"
                name="language"
                data-cy="language"
                type="select"
              >
                {languageValues.map(language => (
                  <option value={language} key={language}>
                    {translate(`bumbaApp.Language.${language}`)}
                  </option>
                ))}
              </ValidatedField>
              <UncontrolledTooltip target="languageLabel">
                <Translate contentKey="bumbaApp.lesson.help.language" />
              </UncontrolledTooltip>
              <ValidatedField label={translate('bumbaApp.lesson.level')} id="lesson-level" name="level" data-cy="level" type="select">
                {difficultyLevelValues.map(difficultyLevel => (
                  <option value={difficultyLevel} key={difficultyLevel}>
                    {translate(`bumbaApp.DifficultyLevel.${difficultyLevel}`)}
                  </option>
                ))}
              </ValidatedField>
              <UncontrolledTooltip target="levelLabel">
                <Translate contentKey="bumbaApp.lesson.help.level" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.lesson.xpReward')}
                id="lesson-xpReward"
                name="xpReward"
                data-cy="xpReward"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <UncontrolledTooltip target="xpRewardLabel">
                <Translate contentKey="bumbaApp.lesson.help.xpReward" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.lesson.passThreshold')}
                id="lesson-passThreshold"
                name="passThreshold"
                data-cy="passThreshold"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  max: { value: 100, message: translate('entity.validation.max', { max: 100 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <UncontrolledTooltip target="passThresholdLabel">
                <Translate contentKey="bumbaApp.lesson.help.passThreshold" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.lesson.createdAt')}
                id="lesson-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <UncontrolledTooltip target="createdAtLabel">
                <Translate contentKey="bumbaApp.lesson.help.createdAt" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.lesson.description')}
                id="lesson-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  maxLength: { value: 250, message: translate('entity.validation.maxlength', { max: 250 }) },
                }}
              />
              <UncontrolledTooltip target="descriptionLabel">
                <Translate contentKey="bumbaApp.lesson.help.description" />
              </UncontrolledTooltip>
              <ValidatedField
                id="lesson-topic"
                name="topic"
                data-cy="topic"
                label={translate('bumbaApp.lesson.topic')}
                type="select"
                required
              >
                <option value="" key="0" />
                {topics
                  ? topics.map(otherEntity => (
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
                id="lesson-createdBy"
                name="createdBy"
                data-cy="createdBy"
                label={translate('bumbaApp.lesson.createdBy')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/lesson" replace color="info">
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

export default LessonUpdate;
