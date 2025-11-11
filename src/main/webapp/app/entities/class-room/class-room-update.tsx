import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row, UncontrolledTooltip } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { Language } from 'app/shared/model/enumerations/language.model';
import { createEntity, getEntity, reset, updateEntity } from './class-room.reducer';

export const ClassRoomUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const classRoomEntity = useAppSelector(state => state.classRoom.entity);
  const loading = useAppSelector(state => state.classRoom.loading);
  const updating = useAppSelector(state => state.classRoom.updating);
  const updateSuccess = useAppSelector(state => state.classRoom.updateSuccess);
  const languageValues = Object.keys(Language);

  const handleClose = () => {
    navigate(`/class-room${location.search}`);
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
    values.createdAt = convertDateTimeToServer(values.createdAt);

    const entity = {
      ...classRoomEntity,
      ...values,
      teacher: users.find(it => it.id.toString() === values.teacher?.toString()),
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
          ...classRoomEntity,
          createdAt: convertDateTimeFromServer(classRoomEntity.createdAt),
          teacher: classRoomEntity?.teacher?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bumbaApp.classRoom.home.createOrEditLabel" data-cy="ClassRoomCreateUpdateHeading">
            <Translate contentKey="bumbaApp.classRoom.home.createOrEditLabel">Create or edit a ClassRoom</Translate>
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
                  id="class-room-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bumbaApp.classRoom.name')}
                id="class-room-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 200, message: translate('entity.validation.maxlength', { max: 200 }) },
                }}
              />
              <UncontrolledTooltip target="nameLabel">
                <Translate contentKey="bumbaApp.classRoom.help.name" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.classRoom.language')}
                id="class-room-language"
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
                <Translate contentKey="bumbaApp.classRoom.help.language" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.classRoom.createdAt')}
                id="class-room-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <UncontrolledTooltip target="createdAtLabel">
                <Translate contentKey="bumbaApp.classRoom.help.createdAt" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.classRoom.description')}
                id="class-room-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <UncontrolledTooltip target="descriptionLabel">
                <Translate contentKey="bumbaApp.classRoom.help.description" />
              </UncontrolledTooltip>
              <ValidatedField
                id="class-room-teacher"
                name="teacher"
                data-cy="teacher"
                label={translate('bumbaApp.classRoom.teacher')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/class-room" replace color="info">
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

export default ClassRoomUpdate;
