import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import { getTeacherDashboard, TeacherDashboard as DashboardData } from '../../services/teacher/teacher-dashboard.service';
import './teacher-dashboard.scss';

export const TeacherDashboard = () => {
  const [dashboard, setDashboard] = useState<DashboardData | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadDashboard();
  }, []);

  const loadDashboard = async () => {
    try {
      setLoading(true);
      const data = await getTeacherDashboard();
      setDashboard(data);
    } catch (error) {
      toast.error('Erro ao carregar dashboard');
      console.error('Error loading dashboard:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="teacher-dashboard">
        <div className="loading-skeleton">
          <div className="skeleton-card"></div>
          <div className="skeleton-card"></div>
          <div className="skeleton-card"></div>
        </div>
      </div>
    );
  }

  if (!dashboard) {
    return (
      <div className="teacher-dashboard">
        <div className="error-message">Erro ao carregar dados</div>
      </div>
    );
  }

  return (
    <div className="teacher-dashboard">
      <div className="dashboard-header">
        <h1>Dashboard do Professor</h1>
      </div>

      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-icon">🏫</div>
          <div className="stat-content">
            <div className="stat-value">{dashboard.totalClasses}</div>
            <div className="stat-label">Turmas</div>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon">👥</div>
          <div className="stat-content">
            <div className="stat-value">{dashboard.totalStudents}</div>
            <div className="stat-label">Alunos</div>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon">📚</div>
          <div className="stat-content">
            <div className="stat-value">{dashboard.totalAssignments}</div>
            <div className="stat-label">Atribuições</div>
          </div>
        </div>
      </div>

      <div className="dashboard-content">
        <div className="classes-section">
          <div className="section-header">
            <h2>Minhas Turmas</h2>
            <Link to="/teacher/classes" className="btn-link">
              Ver todas
            </Link>
          </div>

          <div className="classes-grid">
            {dashboard.classes.map(classRoom => (
              <Link key={classRoom.id} to={`/teacher/classes/${classRoom.id}`} className="class-card">
                <h3>{classRoom.name}</h3>
                <p className="class-description">{classRoom.description}</p>
                <div className="class-stats">
                  <span>👥 {classRoom.studentCount} alunos</span>
                  <span>📚 {classRoom.assignmentCount} atribuições</span>
                  <span>✅ {classRoom.completedLessons} concluídas</span>
                </div>
              </Link>
            ))}
          </div>

          {dashboard.classes.length === 0 && (
            <div className="empty-state">
              <p>Você ainda não tem turmas criadas</p>
              <Link to="/classes/new" className="btn-primary">
                Criar primeira turma
              </Link>
            </div>
          )}
        </div>

        <div className="activities-section">
          <h2>Atividades Recentes</h2>

          <div className="activities-list">
            {dashboard.recentActivities.map((activity, index) => (
              <div key={index} className="activity-item">
                <div className="activity-icon">✅</div>
                <div className="activity-content">
                  <div className="activity-title">
                    <strong>{activity.studentName}</strong> completou <strong>{activity.lessonTitle}</strong>
                  </div>
                  <div className="activity-meta">
                    <span className="activity-score">Pontuação: {activity.score}%</span>
                    <span className="activity-time">{new Date(activity.completedAt).toLocaleDateString('pt-BR')}</span>
                  </div>
                </div>
              </div>
            ))}
          </div>

          {dashboard.recentActivities.length === 0 && (
            <div className="empty-state">
              <p>Nenhuma atividade recente</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default TeacherDashboard;
