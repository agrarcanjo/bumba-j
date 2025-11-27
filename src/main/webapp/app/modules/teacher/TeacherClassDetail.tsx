import React, { useEffect, useState } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { getClassDetail, ClassDetail } from 'app/services/teacher/teacher-class.service';
import { deleteClassRoom } from 'app/services/classroom.service';
import './teacher-class-detail.scss';

export const TeacherClassDetail = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [classDetail, setClassDetail] = useState<ClassDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [deleting, setDeleting] = useState(false);

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

  const handleDelete = async () => {
    if (!classDetail) return;

    const confirmMessage = `Tem certeza que deseja excluir a turma "${classDetail.name}"? Esta ação não pode ser desfeita.`;
    if (!window.confirm(confirmMessage)) return;

    try {
      setDeleting(true);
      await deleteClassRoom(classDetail.id);
      toast.success('Turma excluída com sucesso!');
      navigate('/teacher/classes');
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || 'Erro ao excluir turma';
      toast.error(errorMessage);
      console.error('Error deleting classroom:', error);
    } finally {
      setDeleting(false);
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
          <div className="header-actions">
            <button onClick={handleDelete} className="btn-danger" disabled={deleting}>
              {deleting ? 'Excluindo...' : 'Excluir Turma'}
            </button>
            <Link to={`/teacher/classes/edit/${classDetail.id}`} className="btn-secondary">
              Editar Turma
            </Link>
            <Link to={`/teacher/reports/class/${classDetail.id}`} className="btn-primary">
              Ver Relatório Completo
            </Link>
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
                    <th>Ações</th>
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
                      <td>
                        <Link to={`/teacher/reports/student/${student.id}?classRoomId=${classDetail.id}`} className="btn-view-report">
                          Ver Relatório
                        </Link>
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
