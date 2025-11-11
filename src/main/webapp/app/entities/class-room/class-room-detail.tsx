import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row, UncontrolledTooltip } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './class-room.reducer';

export const ClassRoomDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const classRoomEntity = useAppSelector(state => state.classRoom.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="classRoomDetailsHeading">
          <Translate contentKey="bumbaApp.classRoom.detail.title">ClassRoom</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{classRoomEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="bumbaApp.classRoom.name">Name</Translate>
            </span>
            <UncontrolledTooltip target="name">
              <Translate contentKey="bumbaApp.classRoom.help.name" />
            </UncontrolledTooltip>
          </dt>
          <dd>{classRoomEntity.name}</dd>
          <dt>
            <span id="language">
              <Translate contentKey="bumbaApp.classRoom.language">Language</Translate>
            </span>
            <UncontrolledTooltip target="language">
              <Translate contentKey="bumbaApp.classRoom.help.language" />
            </UncontrolledTooltip>
          </dt>
          <dd>{classRoomEntity.language}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="bumbaApp.classRoom.createdAt">Created At</Translate>
            </span>
            <UncontrolledTooltip target="createdAt">
              <Translate contentKey="bumbaApp.classRoom.help.createdAt" />
            </UncontrolledTooltip>
          </dt>
          <dd>
            {classRoomEntity.createdAt ? <TextFormat value={classRoomEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="description">
              <Translate contentKey="bumbaApp.classRoom.description">Description</Translate>
            </span>
            <UncontrolledTooltip target="description">
              <Translate contentKey="bumbaApp.classRoom.help.description" />
            </UncontrolledTooltip>
          </dt>
          <dd>{classRoomEntity.description}</dd>
          <dt>
            <Translate contentKey="bumbaApp.classRoom.teacher">Teacher</Translate>
          </dt>
          <dd>{classRoomEntity.teacher ? classRoomEntity.teacher.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/class-room" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/class-room/${classRoomEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ClassRoomDetail;
