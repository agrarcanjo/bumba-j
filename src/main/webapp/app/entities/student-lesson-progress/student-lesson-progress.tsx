import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat, Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './student-lesson-progress.reducer';

export const StudentLessonProgress = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const studentLessonProgressList = useAppSelector(state => state.studentLessonProgress.entities);
  const loading = useAppSelector(state => state.studentLessonProgress.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="student-lesson-progress-heading" data-cy="StudentLessonProgressHeading">
        <Translate contentKey="bumbaApp.studentLessonProgress.home.title">Student Lesson Progresses</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="bumbaApp.studentLessonProgress.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/student-lesson-progress/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="bumbaApp.studentLessonProgress.home.createLabel">Create new Student Lesson Progress</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {studentLessonProgressList && studentLessonProgressList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="bumbaApp.studentLessonProgress.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="bumbaApp.studentLessonProgress.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('score')}>
                  <Translate contentKey="bumbaApp.studentLessonProgress.score">Score</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('score')} />
                </th>
                <th className="hand" onClick={sort('xpEarned')}>
                  <Translate contentKey="bumbaApp.studentLessonProgress.xpEarned">Xp Earned</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('xpEarned')} />
                </th>
                <th className="hand" onClick={sort('completedAt')}>
                  <Translate contentKey="bumbaApp.studentLessonProgress.completedAt">Completed At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('completedAt')} />
                </th>
                <th className="hand" onClick={sort('isLate')}>
                  <Translate contentKey="bumbaApp.studentLessonProgress.isLate">Is Late</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isLate')} />
                </th>
                <th>
                  <Translate contentKey="bumbaApp.studentLessonProgress.student">Student</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="bumbaApp.studentLessonProgress.lesson">Lesson</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="bumbaApp.studentLessonProgress.assignment">Assignment</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {studentLessonProgressList.map((studentLessonProgress, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/student-lesson-progress/${studentLessonProgress.id}`} color="link" size="sm">
                      {studentLessonProgress.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`bumbaApp.LessonStatus.${studentLessonProgress.status}`} />
                  </td>
                  <td>{studentLessonProgress.score}</td>
                  <td>{studentLessonProgress.xpEarned}</td>
                  <td>
                    {studentLessonProgress.completedAt ? (
                      <TextFormat type="date" value={studentLessonProgress.completedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{studentLessonProgress.isLate ? 'true' : 'false'}</td>
                  <td>{studentLessonProgress.student ? studentLessonProgress.student.id : ''}</td>
                  <td>
                    {studentLessonProgress.lesson ? (
                      <Link to={`/lesson/${studentLessonProgress.lesson.id}`}>{studentLessonProgress.lesson.title}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {studentLessonProgress.assignment ? (
                      <Link to={`/lesson-assignment/${studentLessonProgress.assignment.id}`}>{studentLessonProgress.assignment.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/student-lesson-progress/${studentLessonProgress.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/student-lesson-progress/${studentLessonProgress.id}/edit`}
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
                        onClick={() => (window.location.href = `/student-lesson-progress/${studentLessonProgress.id}/delete`)}
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
              <Translate contentKey="bumbaApp.studentLessonProgress.home.notFound">No Student Lesson Progresses found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default StudentLessonProgress;
