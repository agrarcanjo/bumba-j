import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row, UncontrolledTooltip } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './question.reducer';

export const QuestionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const questionEntity = useAppSelector(state => state.question.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="questionDetailsHeading">
          <Translate contentKey="bumbaApp.question.detail.title">Question</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{questionEntity.id}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="bumbaApp.question.type">Type</Translate>
            </span>
            <UncontrolledTooltip target="type">
              <Translate contentKey="bumbaApp.question.help.type" />
            </UncontrolledTooltip>
          </dt>
          <dd>{questionEntity.type}</dd>
          <dt>
            <span id="language">
              <Translate contentKey="bumbaApp.question.language">Language</Translate>
            </span>
            <UncontrolledTooltip target="language">
              <Translate contentKey="bumbaApp.question.help.language" />
            </UncontrolledTooltip>
          </dt>
          <dd>{questionEntity.language}</dd>
          <dt>
            <span id="skill">
              <Translate contentKey="bumbaApp.question.skill">Skill</Translate>
            </span>
            <UncontrolledTooltip target="skill">
              <Translate contentKey="bumbaApp.question.help.skill" />
            </UncontrolledTooltip>
          </dt>
          <dd>{questionEntity.skill}</dd>
          <dt>
            <span id="level">
              <Translate contentKey="bumbaApp.question.level">Level</Translate>
            </span>
            <UncontrolledTooltip target="level">
              <Translate contentKey="bumbaApp.question.help.level" />
            </UncontrolledTooltip>
          </dt>
          <dd>{questionEntity.level}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="bumbaApp.question.createdAt">Created At</Translate>
            </span>
            <UncontrolledTooltip target="createdAt">
              <Translate contentKey="bumbaApp.question.help.createdAt" />
            </UncontrolledTooltip>
          </dt>
          <dd>{questionEntity.createdAt ? <TextFormat value={questionEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="prompt">
              <Translate contentKey="bumbaApp.question.prompt">Prompt</Translate>
            </span>
            <UncontrolledTooltip target="prompt">
              <Translate contentKey="bumbaApp.question.help.prompt" />
            </UncontrolledTooltip>
          </dt>
          <dd>{questionEntity.prompt}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="bumbaApp.question.content">Content</Translate>
            </span>
            <UncontrolledTooltip target="content">
              <Translate contentKey="bumbaApp.question.help.content" />
            </UncontrolledTooltip>
          </dt>
          <dd>{questionEntity.content}</dd>
          <dt>
            <span id="assets">
              <Translate contentKey="bumbaApp.question.assets">Assets</Translate>
            </span>
            <UncontrolledTooltip target="assets">
              <Translate contentKey="bumbaApp.question.help.assets" />
            </UncontrolledTooltip>
          </dt>
          <dd>{questionEntity.assets}</dd>
          <dt>
            <Translate contentKey="bumbaApp.question.topic">Topic</Translate>
          </dt>
          <dd>{questionEntity.topic ? questionEntity.topic.name : ''}</dd>
          <dt>
            <Translate contentKey="bumbaApp.question.createdBy">Created By</Translate>
          </dt>
          <dd>{questionEntity.createdBy ? questionEntity.createdBy.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/question" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/question/${questionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default QuestionDetail;
