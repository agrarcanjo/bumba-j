import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row, UncontrolledTooltip } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getLessons } from 'app/entities/lesson/lesson.reducer';
import { createEntity, getEntity, reset, updateEntity } from './recommendation.reducer';

export const RecommendationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const lessons = useAppSelector(state => state.lesson.entities);
  const recommendationEntity = useAppSelector(state => state.recommendation.entity);
  const loading = useAppSelector(state => state.recommendation.loading);
  const updating = useAppSelector(state => state.recommendation.updating);
  const updateSuccess = useAppSelector(state => state.recommendation.updateSuccess);

  const handleClose = () => {
    navigate(`/recommendation${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
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
    values.recommendedAt = convertDateTimeToServer(values.recommendedAt);

    const entity = {
      ...recommendationEntity,
      ...values,
      student: users.find(it => it.id.toString() === values.student?.toString()),
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
          recommendedAt: displayDefaultDateTime(),
        }
      : {
          ...recommendationEntity,
          recommendedAt: convertDateTimeFromServer(recommendationEntity.recommendedAt),
          student: recommendationEntity?.student?.id,
          lesson: recommendationEntity?.lesson?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bumbaApp.recommendation.home.createOrEditLabel" data-cy="RecommendationCreateUpdateHeading">
            <Translate contentKey="bumbaApp.recommendation.home.createOrEditLabel">Create or edit a Recommendation</Translate>
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
                  id="recommendation-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bumbaApp.recommendation.recommendedAt')}
                id="recommendation-recommendedAt"
                name="recommendedAt"
                data-cy="recommendedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <UncontrolledTooltip target="recommendedAtLabel">
                <Translate contentKey="bumbaApp.recommendation.help.recommendedAt" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.recommendation.wasCompleted')}
                id="recommendation-wasCompleted"
                name="wasCompleted"
                data-cy="wasCompleted"
                check
                type="checkbox"
              />
              <UncontrolledTooltip target="wasCompletedLabel">
                <Translate contentKey="bumbaApp.recommendation.help.wasCompleted" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('bumbaApp.recommendation.reason')}
                id="recommendation-reason"
                name="reason"
                data-cy="reason"
                type="text"
                validate={{
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <UncontrolledTooltip target="reasonLabel">
                <Translate contentKey="bumbaApp.recommendation.help.reason" />
              </UncontrolledTooltip>
              <ValidatedField
                id="recommendation-student"
                name="student"
                data-cy="student"
                label={translate('bumbaApp.recommendation.student')}
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
                id="recommendation-lesson"
                name="lesson"
                data-cy="lesson"
                label={translate('bumbaApp.recommendation.lesson')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/recommendation" replace color="info">
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

export default RecommendationUpdate;
