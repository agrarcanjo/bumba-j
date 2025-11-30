import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { getClassReport, ClassReportDTO } from 'app/services/teacher-report.service';
import './teacher-class-report.scss';

export const TeacherClassReport: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [report, setReport] = useState<ClassReportDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadReport();
  }, [id]);

  const loadReport = async () => {
    try {
      setLoading(true);
      const data = await getClassReport(Number(id));
      setReport(data);
    } catch (err: any) {
      setError(err.message || 'Erro ao carregar relatório');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="teacher-class-report">
        <div className="loading">Carregando relatório...</div>
      </div>
    );
  }

  if (error || !report) {
    return (
      <div className="teacher-class-report">
        <div className="error">{error || 'Relatório não encontrado'}</div>
        <Link to="/teacher/classes" className="btn-back">
          Voltar para Turmas
        </Link>
      </div>
    );
  }

  return (
    <div className="teacher-class-report">
      <div className="header">
        <div>
          <h1>{report.classRoomName}</h1>
          <p className="description">{report.description}</p>
        </div>
        <Link to="/teacher/classes" className="btn-back">
          Voltar
        </Link>
      </div>

      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-label">Total de Alunos</div>
          <div className="stat-value">{report.totalStudents}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Atribuições</div>
          <div className="stat-value">{report.totalAssignments}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Progresso Médio</div>
          <div className="stat-value">{report.averageProgress.toFixed(1)}%</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Score Médio</div>
          <div className="stat-value">{report.averageScore.toFixed(1)}</div>
        </div>
      </div>

      <div className="students-section">
        <h2>Progresso dos Alunos</h2>
        {report.studentProgress.length === 0 ? (
          <div className="empty-state">Nenhum aluno matriculado nesta turma.</div>
        ) : (
          <table className="students-table">
            <thead>
              <tr>
                <th>Aluno</th>
                <th>Lições Completadas</th>
                <th>Score Médio</th>
                <th>Streak Atual</th>
                <th>Progresso</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {report.studentProgress.map(student => {
                const progress = report.totalAssignments > 0 ? (student.completedLessons / report.totalAssignments) * 100 : 0;

                return (
                  <tr key={student.studentId}>
                    <td>
                      <div className="student-info">
                        <div className="student-name">
                          {student.firstName} {student.lastName}
                        </div>
                        <div className="student-login">@{student.studentLogin}</div>
                      </div>
                    </td>
                    <td>
                      {student.completedLessons} / {report.totalAssignments}
                    </td>
                    <td>{student.averageScore.toFixed(1)}</td>
                    <td>
                      <span className="streak-badge">{student.currentStreak} dias</span>
                    </td>
                    <td>
                      <div className="progress-bar">
                        <div className="progress-fill" style={{ width: `${progress}%` }}></div>
                      </div>
                      <span className="progress-text">{progress.toFixed(0)}%</span>
                    </td>
                    <td>
                      <Link to={`/teacher/reports/student/${student.studentId}?classRoomId=${id}`} className="btn-view">
                        Ver Detalhes
                      </Link>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
};
