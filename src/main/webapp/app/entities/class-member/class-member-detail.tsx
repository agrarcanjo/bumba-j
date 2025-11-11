import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row, UncontrolledTooltip } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './class-member.reducer';

export const ClassMemberDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const classMemberEntity = useAppSelector(state => state.classMember.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="classMemberDetailsHeading">
          <Translate contentKey="bumbaApp.classMember.detail.title">ClassMember</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{classMemberEntity.id}</dd>
          <dt>
            <span id="enrolledAt">
              <Translate contentKey="bumbaApp.classMember.enrolledAt">Enrolled At</Translate>
            </span>
            <UncontrolledTooltip target="enrolledAt">
              <Translate contentKey="bumbaApp.classMember.help.enrolledAt" />
            </UncontrolledTooltip>
          </dt>
          <dd>
            {classMemberEntity.enrolledAt ? <TextFormat value={classMemberEntity.enrolledAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="bumbaApp.classMember.classRoom">Class Room</Translate>
          </dt>
          <dd>{classMemberEntity.classRoom ? classMemberEntity.classRoom.name : ''}</dd>
          <dt>
            <Translate contentKey="bumbaApp.classMember.student">Student</Translate>
          </dt>
          <dd>{classMemberEntity.student ? classMemberEntity.student.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/class-member" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/class-member/${classMemberEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ClassMemberDetail;
