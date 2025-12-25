import React, { useState, useEffect } from 'react';
import { useParams, useSearchParams, Link } from 'react-router-dom';
import { getStudentReport, StudentReportDTO } from 'app/services/teacher-report.service';
import './teacher-student-report.scss';

export const TeacherStudentReport: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [searchParams] = useSearchParams();
  const classRoomId = searchParams.get('classRoomId');
  const [report, setReport] = useState<StudentReportDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadReport();
  }, [id, classRoomId]);

  const loadReport = async () => {
    try {
      setLoading(true);
      const data = await getStudentReport(Number(id), classRoomId ? Number(classRoomId) : undefined);
      setReport(data);
    } catch (err: any) {
      setError(err.message || 'Erro ao carregar relatório');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="teacher-student-report">
        <div className="loading">Carregando relatório...</div>
      </div>
    );
  }

  if (error || !report) {
    return (
      <div className="teacher-student-report">
        <div className="error">{error || 'Relatório não encontrado'}</div>
        <Link to={classRoomId ? `/teacher/reports/class/${classRoomId}` : '/teacher/classes'} className="btn-back">
          Voltar
        </Link>
      </div>
    );
  }

  return (
    <div className="teacher-student-report">
      <div className="header">
        <div>
          <h1>
            {report.firstName} {report.lastName}
          </h1>
          <p className="student-info">
            @{report.studentLogin} • {report.email}
          </p>
        </div>
        <Link to={classRoomId ? `/teacher/reports/class/${classRoomId}` : '/teacher/classes'} className="btn-back">
          Voltar
        </Link>
      </div>

      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-label">XP Total</div>
          <div className="stat-value">{report.totalXp}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Nível</div>
          <div className="stat-value">{report.level}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Streak Atual</div>
          <div className="stat-value">{report.currentStreak} dias</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Lições Completadas</div>
          <div className="stat-value">{report.completedLessons}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Score Médio</div>
          <div className="stat-value">{report.averageScore.toFixed(1)}</div>
        </div>
      </div>

      <div className="lessons-section">
        <h2>Histórico de Lições</h2>
        {report.lessonProgress.length === 0 ? (
          <div className="empty-state">Nenhuma lição completada ainda.</div>
        ) : (
          <table className="lessons-table">
            <thead>
              <tr>
                <th>Lição</th>
                <th>Data de Conclusão</th>
                <th>Score</th>
                <th>XP Ganho</th>
                <th>Tentativas</th>
              </tr>
            </thead>
            <tbody>
              {report.lessonProgress.map((lesson, index) => (
                <tr key={index}>
                  <td>{lesson.lessonTitle}</td>
                  <td>{new Date(lesson.completedAt).toLocaleDateString('pt-BR')}</td>
                  <td>
                    <span className={`score-badge ${getScoreClass(lesson.score)}`}>{lesson.score}</span>
                  </td>
                  <td>
                    <span className="xp-badge">+{lesson.xpGained} XP</span>
                  </td>
                  <td>{lesson.attempts}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
};

const getScoreClass = (score: number): string => {
  if (score >= 90) return 'excellent';
  if (score >= 70) return 'good';
  if (score >= 50) return 'average';
  return 'poor';
};
