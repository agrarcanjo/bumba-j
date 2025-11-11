import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row, UncontrolledTooltip } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { UserLevel } from 'app/shared/model/enumerations/user-level.model';
import { createEntity, getEntity, reset, updateEntity } from './user-profile.reducer';

export const UserProfileUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const userProfileEntity = useAppSelector(state => state.userProfile.entity);
  const loading = useAppSelector(state => state.userProfile.loading);
  const updating = useAppSelector(state => state.userProfile.updating);
  const updateSuccess = useAppSelector(state => state.userProfile.updateSuccess);
  const userLevelValues = Object.keys(UserLevel);

  const handleClose = () => {
    navigate('/user-profile');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

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
    if (values.totalXp !== undefined && typeof values.totalXp !== 'number') {
      values.totalXp = Number(values.totalXp);
    }
    if (values.currentStreak !== undefined && typeof values.currentStreak !== 'number') {
      values.currentStreak = Number(values.currentStreak);
    }
    if (values.dailyGoalXp !== undefined && typeof values.dailyGoalXp !== 'number') {
      values.dailyGoalXp = Number(values.dailyGoalXp);
    }

    const entity = {
      ...userProfileEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user?.toString()),
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
          currentLevel: 'BEGINNER',
          ...userProfileEntity,
          user: userProfileEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bumbaApp.userProfile.home.createOrEditLabel" data-cy="UserProfileCreateUpdateHeading">
            <Translate contentKey="bumbaApp.userProfile.home.createOrEditLabel">Create or edit a UserProfile</Translate>
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
                  id="user-profile-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bumbaApp.userProfile.municipalityCode')}
                id="user-profile-municipalityCode"
                name="municipalityCode"
                data-cy="municipalityCode"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 7, message: translate('entity.validation.maxlength', { max: 7 }) },
                }}
              />
              <UncontrolledTooltip target="municipalityCodeLabel">
                <Translate contentKey="bumbaApp.userProfile.help.municipalityCode" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.userProfile.currentLevel')}
                id="user-profile-currentLevel"
                name="currentLevel"
                data-cy="currentLevel"
                type="select"
              >
                {userLevelValues.map(userLevel => (
                  <option value={userLevel} key={userLevel}>
                    {translate(`bumbaApp.UserLevel.${userLevel}`)}
                  </option>
                ))}
              </ValidatedField>
              <UncontrolledTooltip target="currentLevelLabel">
                <Translate contentKey="bumbaApp.userProfile.help.currentLevel" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.userProfile.totalXp')}
                id="user-profile-totalXp"
                name="totalXp"
                data-cy="totalXp"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <UncontrolledTooltip target="totalXpLabel">
                <Translate contentKey="bumbaApp.userProfile.help.totalXp" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.userProfile.currentStreak')}
                id="user-profile-currentStreak"
                name="currentStreak"
                data-cy="currentStreak"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <UncontrolledTooltip target="currentStreakLabel">
                <Translate contentKey="bumbaApp.userProfile.help.currentStreak" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.userProfile.dailyGoalXp')}
                id="user-profile-dailyGoalXp"
                name="dailyGoalXp"
                data-cy="dailyGoalXp"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 10, message: translate('entity.validation.min', { min: 10 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <UncontrolledTooltip target="dailyGoalXpLabel">
                <Translate contentKey="bumbaApp.userProfile.help.dailyGoalXp" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.userProfile.lastActivityDate')}
                id="user-profile-lastActivityDate"
                name="lastActivityDate"
                data-cy="lastActivityDate"
                type="date"
              />
              <UncontrolledTooltip target="lastActivityDateLabel">
                <Translate contentKey="bumbaApp.userProfile.help.lastActivityDate" />
              </UncontrolledTooltip>
              <ValidatedField
                id="user-profile-user"
                name="user"
                data-cy="user"
                label={translate('bumbaApp.userProfile.user')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-profile" replace color="info">
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

export default UserProfileUpdate;
