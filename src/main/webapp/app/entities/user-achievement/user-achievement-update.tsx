import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row, UncontrolledTooltip } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getAchievements } from 'app/entities/achievement/achievement.reducer';
import { createEntity, getEntity, reset, updateEntity } from './user-achievement.reducer';

export const UserAchievementUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const achievements = useAppSelector(state => state.achievement.entities);
  const userAchievementEntity = useAppSelector(state => state.userAchievement.entity);
  const loading = useAppSelector(state => state.userAchievement.loading);
  const updating = useAppSelector(state => state.userAchievement.updating);
  const updateSuccess = useAppSelector(state => state.userAchievement.updateSuccess);

  const handleClose = () => {
    navigate('/user-achievement');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getAchievements({}));
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
    values.unlockedAt = convertDateTimeToServer(values.unlockedAt);

    const entity = {
      ...userAchievementEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user?.toString()),
      achievement: achievements.find(it => it.id.toString() === values.achievement?.toString()),
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
          unlockedAt: displayDefaultDateTime(),
        }
      : {
          ...userAchievementEntity,
          unlockedAt: convertDateTimeFromServer(userAchievementEntity.unlockedAt),
          user: userAchievementEntity?.user?.id,
          achievement: userAchievementEntity?.achievement?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bumbaApp.userAchievement.home.createOrEditLabel" data-cy="UserAchievementCreateUpdateHeading">
            <Translate contentKey="bumbaApp.userAchievement.home.createOrEditLabel">Create or edit a UserAchievement</Translate>
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
                  id="user-achievement-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bumbaApp.userAchievement.unlockedAt')}
                id="user-achievement-unlockedAt"
                name="unlockedAt"
                data-cy="unlockedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <UncontrolledTooltip target="unlockedAtLabel">
                <Translate contentKey="bumbaApp.userAchievement.help.unlockedAt" />
              </UncontrolledTooltip>
              <ValidatedField
                id="user-achievement-user"
                name="user"
                data-cy="user"
                label={translate('bumbaApp.userAchievement.user')}
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
                id="user-achievement-achievement"
                name="achievement"
                data-cy="achievement"
                label={translate('bumbaApp.userAchievement.achievement')}
                type="select"
                required
              >
                <option value="" key="0" />
                {achievements
                  ? achievements.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-achievement" replace color="info">
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

export default UserAchievementUpdate;
