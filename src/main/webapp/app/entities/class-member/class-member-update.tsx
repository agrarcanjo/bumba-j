import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row, UncontrolledTooltip } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getClassRooms } from 'app/entities/class-room/class-room.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { createEntity, getEntity, reset, updateEntity } from './class-member.reducer';

export const ClassMemberUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const classRooms = useAppSelector(state => state.classRoom.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const classMemberEntity = useAppSelector(state => state.classMember.entity);
  const loading = useAppSelector(state => state.classMember.loading);
  const updating = useAppSelector(state => state.classMember.updating);
  const updateSuccess = useAppSelector(state => state.classMember.updateSuccess);

  const handleClose = () => {
    navigate('/class-member');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getClassRooms({}));
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
    values.enrolledAt = convertDateTimeToServer(values.enrolledAt);

    const entity = {
      ...classMemberEntity,
      ...values,
      classRoom: classRooms.find(it => it.id.toString() === values.classRoom?.toString()),
      student: users.find(it => it.id.toString() === values.student?.toString()),
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
          enrolledAt: displayDefaultDateTime(),
        }
      : {
          ...classMemberEntity,
          enrolledAt: convertDateTimeFromServer(classMemberEntity.enrolledAt),
          classRoom: classMemberEntity?.classRoom?.id,
          student: classMemberEntity?.student?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bumbaApp.classMember.home.createOrEditLabel" data-cy="ClassMemberCreateUpdateHeading">
            <Translate contentKey="bumbaApp.classMember.home.createOrEditLabel">Create or edit a ClassMember</Translate>
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
                  id="class-member-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bumbaApp.classMember.enrolledAt')}
                id="class-member-enrolledAt"
                name="enrolledAt"
                data-cy="enrolledAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <UncontrolledTooltip target="enrolledAtLabel">
                <Translate contentKey="bumbaApp.classMember.help.enrolledAt" />
              </UncontrolledTooltip>
              <ValidatedField
                id="class-member-classRoom"
                name="classRoom"
                data-cy="classRoom"
                label={translate('bumbaApp.classMember.classRoom')}
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
                id="class-member-student"
                name="student"
                data-cy="student"
                label={translate('bumbaApp.classMember.student')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/class-member" replace color="info">
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

export default ClassMemberUpdate;
