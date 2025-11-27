import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import { getClassDetail, ClassDetail } from 'app/services/teacher/teacher-class.service';
import './teacher-class-detail.scss';

export const TeacherClassDetail = () => {
  const { id } = useParams<{ id: string }>();
  const [classDetail, setClassDetail] = useState<ClassDetail | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (id) {
      loadClassDetail(parseInt(id, 10));
    }
  }, [id]);

  const loadClassDetail = async (classId: number) => {
    try {
      setLoading(true);
      const data = await getClassDetail(classId);
      setClassDetail(data);
    } catch (error) {
      toast.error('Erro ao carregar detalhes da turma');
      console.error('Error loading class detail:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="teacher-class-detail">
        <div className="loading-skeleton">
          <div className="skeleton-header"></div>
          <div className="skeleton-content"></div>
        </div>
      </div>
    );
  }

  if (!classDetail) {
    return (
      <div className="teacher-class-detail">
        <div className="error-message">Turma não encontrada</div>
      </div>
    );
  }

  return (
    <div className="teacher-class-detail">
      <div className="detail-header">
        <div className="header-content">
          <Link to="/teacher/classes" className="back-link">
            ← Voltar
          </Link>
          <h1>{classDetail.name}</h1>
          <p className="class-description">{classDetail.description}</p>
          <div className="class-meta">
            <span className="meta-item">
              <strong>Idioma:</strong> {classDetail.language}
            </span>
            <span className="meta-item">
              <strong>Criada em:</strong> {new Date(classDetail.createdAt).toLocaleDateString('pt-BR')}
            </span>
          </div>
        </div>
      </div>

      <div className="detail-content">
        <div className="students-section">
          <div className="section-header">
            <h2>Alunos ({classDetail.students.length})</h2>
            <Link to={`/class-member?classId=${classDetail.id}`} className="btn-secondary">
              + Adicionar Aluno
            </Link>
          </div>

          {classDetail.students.length === 0 ? (
            <div className="empty-state">
              <p>Nenhum aluno matriculado nesta turma</p>
            </div>
          ) : (
            <div className="students-table">
              <table>
                <thead>
                  <tr>
                    <th>Nome</th>
                    <th>Login</th>
                    <th>Data de Entrada</th>
                    <th>Lições Concluídas</th>
                  </tr>
                </thead>
                <tbody>
                  {classDetail.students.map(student => (
                    <tr key={student.id}>
                      <td>
                        {student.firstName} {student.lastName}
                      </td>
                      <td>{student.login}</td>
                      <td>{new Date(student.joinedAt).toLocaleDateString('pt-BR')}</td>
                      <td>
                        <span className="badge">{student.completedLessons}</span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>

        <div className="assignments-section">
          <div className="section-header">
            <h2>Atribuições ({classDetail.assignments.length})</h2>
            <Link to={`/lesson-assignment/new?classId=${classDetail.id}`} className="btn-secondary">
              + Nova Atribuição
            </Link>
          </div>

          {classDetail.assignments.length === 0 ? (
            <div className="empty-state">
              <p>Nenhuma atribuição criada para esta turma</p>
            </div>
          ) : (
            <div className="assignments-list">
              {classDetail.assignments.map(assignment => (
                <div key={assignment.id} className="assignment-card">
                  <div className="assignment-info">
                    <div className="assignment-dates">
                      <span>
                        <strong>Atribuída em:</strong> {new Date(assignment.assignedAt).toLocaleDateString('pt-BR')}
                      </span>
                      <span>
                        <strong>Prazo:</strong> {new Date(assignment.dueDate).toLocaleDateString('pt-BR')}
                      </span>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default TeacherClassDetail;
