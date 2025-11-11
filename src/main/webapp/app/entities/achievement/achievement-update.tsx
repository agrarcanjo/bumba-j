import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row, UncontrolledTooltip } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './achievement.reducer';

export const AchievementUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const achievementEntity = useAppSelector(state => state.achievement.entity);
  const loading = useAppSelector(state => state.achievement.loading);
  const updating = useAppSelector(state => state.achievement.updating);
  const updateSuccess = useAppSelector(state => state.achievement.updateSuccess);

  const handleClose = () => {
    navigate('/achievement');
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
      ...achievementEntity,
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
          ...achievementEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bumbaApp.achievement.home.createOrEditLabel" data-cy="AchievementCreateUpdateHeading">
            <Translate contentKey="bumbaApp.achievement.home.createOrEditLabel">Create or edit a Achievement</Translate>
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
                  id="achievement-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bumbaApp.achievement.code')}
                id="achievement-code"
                name="code"
                data-cy="code"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <UncontrolledTooltip target="codeLabel">
                <Translate contentKey="bumbaApp.achievement.help.code" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.achievement.name')}
                id="achievement-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <UncontrolledTooltip target="nameLabel">
                <Translate contentKey="bumbaApp.achievement.help.name" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.achievement.iconUrl')}
                id="achievement-iconUrl"
                name="iconUrl"
                data-cy="iconUrl"
                type="text"
                validate={{
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <UncontrolledTooltip target="iconUrlLabel">
                <Translate contentKey="bumbaApp.achievement.help.iconUrl" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.achievement.description')}
                id="achievement-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  maxLength: { value: 250, message: translate('entity.validation.maxlength', { max: 250 }) },
                }}
              />
              <UncontrolledTooltip target="descriptionLabel">
                <Translate contentKey="bumbaApp.achievement.help.description" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.achievement.criteria')}
                id="achievement-criteria"
                name="criteria"
                data-cy="criteria"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <UncontrolledTooltip target="criteriaLabel">
                <Translate contentKey="bumbaApp.achievement.help.criteria" />
              </UncontrolledTooltip>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/achievement" replace color="info">
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

export default AchievementUpdate;
