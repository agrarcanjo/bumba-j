import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row, UncontrolledTooltip } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { Language } from 'app/shared/model/enumerations/language.model';
import { createEntity, getEntity, reset, updateEntity } from './topic.reducer';

export const TopicUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const topicEntity = useAppSelector(state => state.topic.entity);
  const loading = useAppSelector(state => state.topic.loading);
  const updating = useAppSelector(state => state.topic.updating);
  const updateSuccess = useAppSelector(state => state.topic.updateSuccess);
  const languageValues = Object.keys(Language);

  const handleClose = () => {
    navigate('/topic');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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

    const entity = {
      ...topicEntity,
      ...values,
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
          language: 'ENGLISH',
          ...topicEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bumbaApp.topic.home.createOrEditLabel" data-cy="TopicCreateUpdateHeading">
            <Translate contentKey="bumbaApp.topic.home.createOrEditLabel">Create or edit a Topic</Translate>
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
                  id="topic-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bumbaApp.topic.name')}
                id="topic-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <UncontrolledTooltip target="nameLabel">
                <Translate contentKey="bumbaApp.topic.help.name" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.topic.language')}
                id="topic-language"
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
                <Translate contentKey="bumbaApp.topic.help.language" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.topic.description')}
                id="topic-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <UncontrolledTooltip target="descriptionLabel">
                <Translate contentKey="bumbaApp.topic.help.description" />
              </UncontrolledTooltip>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/topic" replace color="info">
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

export default TopicUpdate;
