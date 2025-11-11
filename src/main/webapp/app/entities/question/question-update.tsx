import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row, UncontrolledTooltip } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getTopics } from 'app/entities/topic/topic.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { QuestionType } from 'app/shared/model/enumerations/question-type.model';
import { Language } from 'app/shared/model/enumerations/language.model';
import { Skill } from 'app/shared/model/enumerations/skill.model';
import { DifficultyLevel } from 'app/shared/model/enumerations/difficulty-level.model';
import { createEntity, getEntity, reset, updateEntity } from './question.reducer';

export const QuestionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const topics = useAppSelector(state => state.topic.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const questionEntity = useAppSelector(state => state.question.entity);
  const loading = useAppSelector(state => state.question.loading);
  const updating = useAppSelector(state => state.question.updating);
  const updateSuccess = useAppSelector(state => state.question.updateSuccess);
  const questionTypeValues = Object.keys(QuestionType);
  const languageValues = Object.keys(Language);
  const skillValues = Object.keys(Skill);
  const difficultyLevelValues = Object.keys(DifficultyLevel);

  const handleClose = () => {
    navigate(`/question${location.search}`);
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
    values.createdAt = convertDateTimeToServer(values.createdAt);

    const entity = {
      ...questionEntity,
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
          type: 'MULTIPLE_CHOICE',
          language: 'ENGLISH',
          skill: 'READING',
          level: 'EASY',
          ...questionEntity,
          createdAt: convertDateTimeFromServer(questionEntity.createdAt),
          topic: questionEntity?.topic?.id,
          createdBy: questionEntity?.createdBy?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bumbaApp.question.home.createOrEditLabel" data-cy="QuestionCreateUpdateHeading">
            <Translate contentKey="bumbaApp.question.home.createOrEditLabel">Create or edit a Question</Translate>
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
                  id="question-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('bumbaApp.question.type')} id="question-type" name="type" data-cy="type" type="select">
                {questionTypeValues.map(questionType => (
                  <option value={questionType} key={questionType}>
                    {translate(`bumbaApp.QuestionType.${questionType}`)}
                  </option>
                ))}
              </ValidatedField>
              <UncontrolledTooltip target="typeLabel">
                <Translate contentKey="bumbaApp.question.help.type" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.question.language')}
                id="question-language"
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
                <Translate contentKey="bumbaApp.question.help.language" />
              </UncontrolledTooltip>
              <ValidatedField label={translate('bumbaApp.question.skill')} id="question-skill" name="skill" data-cy="skill" type="select">
                {skillValues.map(skill => (
                  <option value={skill} key={skill}>
                    {translate(`bumbaApp.Skill.${skill}`)}
                  </option>
                ))}
              </ValidatedField>
              <UncontrolledTooltip target="skillLabel">
                <Translate contentKey="bumbaApp.question.help.skill" />
              </UncontrolledTooltip>
              <ValidatedField label={translate('bumbaApp.question.level')} id="question-level" name="level" data-cy="level" type="select">
                {difficultyLevelValues.map(difficultyLevel => (
                  <option value={difficultyLevel} key={difficultyLevel}>
                    {translate(`bumbaApp.DifficultyLevel.${difficultyLevel}`)}
                  </option>
                ))}
              </ValidatedField>
              <UncontrolledTooltip target="levelLabel">
                <Translate contentKey="bumbaApp.question.help.level" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.question.createdAt')}
                id="question-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <UncontrolledTooltip target="createdAtLabel">
                <Translate contentKey="bumbaApp.question.help.createdAt" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.question.prompt')}
                id="question-prompt"
                name="prompt"
                data-cy="prompt"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 2000, message: translate('entity.validation.maxlength', { max: 2000 }) },
                }}
              />
              <UncontrolledTooltip target="promptLabel">
                <Translate contentKey="bumbaApp.question.help.prompt" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.question.content')}
                id="question-content"
                name="content"
                data-cy="content"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <UncontrolledTooltip target="contentLabel">
                <Translate contentKey="bumbaApp.question.help.content" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.question.assets')}
                id="question-assets"
                name="assets"
                data-cy="assets"
                type="textarea"
              />
              <UncontrolledTooltip target="assetsLabel">
                <Translate contentKey="bumbaApp.question.help.assets" />
              </UncontrolledTooltip>
              <ValidatedField
                id="question-topic"
                name="topic"
                data-cy="topic"
                label={translate('bumbaApp.question.topic')}
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
                id="question-createdBy"
                name="createdBy"
                data-cy="createdBy"
                label={translate('bumbaApp.question.createdBy')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/question" replace color="info">
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

export default QuestionUpdate;
