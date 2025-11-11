import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './attempt.reducer';

export const Attempt = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const attemptList = useAppSelector(state => state.attempt.entities);
  const loading = useAppSelector(state => state.attempt.loading);
  const totalItems = useAppSelector(state => state.attempt.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="attempt-heading" data-cy="AttemptHeading">
        <Translate contentKey="bumbaApp.attempt.home.title">Attempts</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="bumbaApp.attempt.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/attempt/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="bumbaApp.attempt.home.createLabel">Create new Attempt</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {attemptList && attemptList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="bumbaApp.attempt.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('isCorrect')}>
                  <Translate contentKey="bumbaApp.attempt.isCorrect">Is Correct</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isCorrect')} />
                </th>
                <th className="hand" onClick={sort('timeSpentSeconds')}>
                  <Translate contentKey="bumbaApp.attempt.timeSpentSeconds">Time Spent Seconds</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('timeSpentSeconds')} />
                </th>
                <th className="hand" onClick={sort('attemptedAt')}>
                  <Translate contentKey="bumbaApp.attempt.attemptedAt">Attempted At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('attemptedAt')} />
                </th>
                <th className="hand" onClick={sort('answer')}>
                  <Translate contentKey="bumbaApp.attempt.answer">Answer</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('answer')} />
                </th>
                <th className="hand" onClick={sort('metadata')}>
                  <Translate contentKey="bumbaApp.attempt.metadata">Metadata</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('metadata')} />
                </th>
                <th>
                  <Translate contentKey="bumbaApp.attempt.student">Student</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="bumbaApp.attempt.question">Question</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="bumbaApp.attempt.lesson">Lesson</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {attemptList.map((attempt, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/attempt/${attempt.id}`} color="link" size="sm">
                      {attempt.id}
                    </Button>
                  </td>
                  <td>{attempt.isCorrect ? 'true' : 'false'}</td>
                  <td>{attempt.timeSpentSeconds}</td>
                  <td>{attempt.attemptedAt ? <TextFormat type="date" value={attempt.attemptedAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{attempt.answer}</td>
                  <td>{attempt.metadata}</td>
                  <td>{attempt.student ? attempt.student.id : ''}</td>
                  <td>{attempt.question ? <Link to={`/question/${attempt.question.id}`}>{attempt.question.id}</Link> : ''}</td>
                  <td>{attempt.lesson ? <Link to={`/lesson/${attempt.lesson.id}`}>{attempt.lesson.title}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/attempt/${attempt.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/attempt/${attempt.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/attempt/${attempt.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="bumbaApp.attempt.home.notFound">No Attempts found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={attemptList && attemptList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Attempt;
