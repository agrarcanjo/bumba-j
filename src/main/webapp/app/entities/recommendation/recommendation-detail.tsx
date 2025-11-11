import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row, UncontrolledTooltip } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './recommendation.reducer';

export const RecommendationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const recommendationEntity = useAppSelector(state => state.recommendation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="recommendationDetailsHeading">
          <Translate contentKey="bumbaApp.recommendation.detail.title">Recommendation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{recommendationEntity.id}</dd>
          <dt>
            <span id="recommendedAt">
              <Translate contentKey="bumbaApp.recommendation.recommendedAt">Recommended At</Translate>
            </span>
            <UncontrolledTooltip target="recommendedAt">
              <Translate contentKey="bumbaApp.recommendation.help.recommendedAt" />
            </UncontrolledTooltip>
          </dt>
          <dd>
            {recommendationEntity.recommendedAt ? (
              <TextFormat value={recommendationEntity.recommendedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="wasCompleted">
              <Translate contentKey="bumbaApp.recommendation.wasCompleted">Was Completed</Translate>
            </span>
            <UncontrolledTooltip target="wasCompleted">
              <Translate contentKey="bumbaApp.recommendation.help.wasCompleted" />
            </UncontrolledTooltip>
          </dt>
          <dd>{recommendationEntity.wasCompleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="reason">
              <Translate contentKey="bumbaApp.recommendation.reason">Reason</Translate>
            </span>
            <UncontrolledTooltip target="reason">
              <Translate contentKey="bumbaApp.recommendation.help.reason" />
            </UncontrolledTooltip>
          </dt>
          <dd>{recommendationEntity.reason}</dd>
          <dt>
            <Translate contentKey="bumbaApp.recommendation.student">Student</Translate>
          </dt>
          <dd>{recommendationEntity.student ? recommendationEntity.student.id : ''}</dd>
          <dt>
            <Translate contentKey="bumbaApp.recommendation.lesson">Lesson</Translate>
          </dt>
          <dd>{recommendationEntity.lesson ? recommendationEntity.lesson.title : ''}</dd>
        </dl>
        <Button tag={Link} to="/recommendation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/recommendation/${recommendationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RecommendationDetail;
