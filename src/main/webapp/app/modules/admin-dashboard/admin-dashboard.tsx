import React, { useEffect, useState } from 'react';
import { Card, Row, Col, Statistic, Table, Typography, Spin } from 'antd';
import { UserOutlined, BookOutlined, TeamOutlined, TrophyOutlined, CheckCircleOutlined, ClockCircleOutlined } from '@ant-design/icons';
import axios from 'axios';

const { Title } = Typography;

interface Metrics {
  totalUsers: number;
  totalStudents: number;
  totalTeachers: number;
  totalLessons: number;
  totalQuestions: number;
  totalClassrooms: number;
  totalAttempts: number;
  totalAchievements: number;
  activeUsers: number;
  completedAttempts: number;
}

export const AdminDashboard = () => {
  const [metrics, setMetrics] = useState<Metrics | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadMetrics();
  }, []);

  const loadMetrics = async () => {
    try {
      const response = await axios.get<Metrics>('/api/admin/metrics');
      setMetrics(response.data);
    } catch (error) {
      console.error('Error loading metrics:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div style={{ textAlign: 'center', padding: '50px' }}>
        <Spin size="large" />
      </div>
    );
  }

  if (!metrics) {
    return <div>Erro ao carregar métricas</div>;
  }

  return (
    <div style={{ padding: '24px' }}>
      <Title level={2}>Dashboard Administrativo</Title>

      <Row gutter={[16, 16]} style={{ marginTop: '24px' }}>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic title="Total de Usuários" value={metrics.totalUsers} prefix={<UserOutlined />} valueStyle={{ color: '#3f8600' }} />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="Usuários Ativos"
              value={metrics.activeUsers}
              prefix={<CheckCircleOutlined />}
              valueStyle={{ color: '#1890ff' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic title="Total de Alunos" value={metrics.totalStudents} prefix={<TeamOutlined />} valueStyle={{ color: '#722ed1' }} />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="Total de Professores"
              value={metrics.totalTeachers}
              prefix={<UserOutlined />}
              valueStyle={{ color: '#eb2f96' }}
            />
          </Card>
        </Col>
      </Row>

      <Row gutter={[16, 16]} style={{ marginTop: '16px' }}>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic title="Total de Lições" value={metrics.totalLessons} prefix={<BookOutlined />} valueStyle={{ color: '#fa8c16' }} />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="Total de Questões"
              value={metrics.totalQuestions}
              prefix={<BookOutlined />}
              valueStyle={{ color: '#13c2c2' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="Total de Turmas"
              value={metrics.totalClassrooms}
              prefix={<TeamOutlined />}
              valueStyle={{ color: '#52c41a' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="Total de Conquistas"
              value={metrics.totalAchievements}
              prefix={<TrophyOutlined />}
              valueStyle={{ color: '#faad14' }}
            />
          </Card>
        </Col>
      </Row>

      <Row gutter={[16, 16]} style={{ marginTop: '16px' }}>
        <Col xs={24} sm={12}>
          <Card>
            <Statistic
              title="Total de Tentativas"
              value={metrics.totalAttempts}
              prefix={<ClockCircleOutlined />}
              valueStyle={{ color: '#2f54eb' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12}>
          <Card>
            <Statistic
              title="Tentativas Completas"
              value={metrics.completedAttempts}
              prefix={<CheckCircleOutlined />}
              valueStyle={{ color: '#52c41a' }}
            />
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default AdminDashboard;
