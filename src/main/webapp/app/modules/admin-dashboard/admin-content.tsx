import React, { useEffect, useState } from 'react';
import { Tabs, Table, Button, Modal, Form, Input, InputNumber, Select, Space, Typography, message, Popconfirm } from 'antd';
import { EditOutlined, DeleteOutlined, PlusOutlined, CheckOutlined, CloseOutlined } from '@ant-design/icons';
import axios from 'axios';

const { Title } = Typography;
const { TabPane } = Tabs;
const { TextArea } = Input;

interface Topic {
  id: number;
  name: string;
  description: string;
}

interface Achievement {
  id: number;
  name: string;
  description: string;
  icon: string;
  requiredPoints: number;
}

interface Question {
  id: number;
  questionText: string;
  difficulty: string;
  topic: { id: number; name: string };
}

export const AdminContent = () => {
  const [topics, setTopics] = useState<Topic[]>([]);
  const [achievements, setAchievements] = useState<Achievement[]>([]);
  const [pendingQuestions, setPendingQuestions] = useState<Question[]>([]);
  const [loading, setLoading] = useState(false);
  const [topicModalVisible, setTopicModalVisible] = useState(false);
  const [achievementModalVisible, setAchievementModalVisible] = useState(false);
  const [editingTopic, setEditingTopic] = useState<Topic | null>(null);
  const [editingAchievement, setEditingAchievement] = useState<Achievement | null>(null);
  const [topicForm] = Form.useForm();
  const [achievementForm] = Form.useForm();

  useEffect(() => {
    loadTopics();
    loadAchievements();
    loadPendingQuestions();
  }, []);

  const loadTopics = async () => {
    setLoading(true);
    try {
      const response = await axios.get<Topic[]>('/api/admin/content/topics');
      setTopics(response.data);
    } catch (error) {
      message.error('Erro ao carregar tópicos');
      console.error('Error loading topics:', error);
    } finally {
      setLoading(false);
    }
  };

  const loadAchievements = async () => {
    setLoading(true);
    try {
      const response = await axios.get<Achievement[]>('/api/admin/content/achievements');
      setAchievements(response.data);
    } catch (error) {
      message.error('Erro ao carregar conquistas');
      console.error('Error loading achievements:', error);
    } finally {
      setLoading(false);
    }
  };

  const loadPendingQuestions = async () => {
    setLoading(true);
    try {
      const response = await axios.get<Question[]>('/api/admin/content/questions/pending');
      setPendingQuestions(response.data);
    } catch (error) {
      message.error('Erro ao carregar questões pendentes');
      console.error('Error loading pending questions:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateTopic = () => {
    setEditingTopic(null);
    topicForm.resetFields();
    setTopicModalVisible(true);
  };

  const handleEditTopic = (topic: Topic) => {
    setEditingTopic(topic);
    topicForm.setFieldsValue(topic);
    setTopicModalVisible(true);
  };

  const handleSaveTopic = async () => {
    try {
      const values = await topicForm.validateFields();
      if (editingTopic) {
        await axios.put(`/api/admin/content/topics/${editingTopic.id}`, { ...values, id: editingTopic.id });
        message.success('Tópico atualizado com sucesso');
      } else {
        await axios.post('/api/admin/content/topics', values);
        message.success('Tópico criado com sucesso');
      }
      setTopicModalVisible(false);
      loadTopics();
    } catch (error) {
      message.error('Erro ao salvar tópico');
      console.error('Error saving topic:', error);
    }
  };

  const handleDeleteTopic = async (topicId: number) => {
    try {
      await axios.delete(`/api/admin/content/topics/${topicId}`);
      message.success('Tópico excluído com sucesso');
      loadTopics();
    } catch (error) {
      message.error('Erro ao excluir tópico');
      console.error('Error deleting topic:', error);
    }
  };

  const handleCreateAchievement = () => {
    setEditingAchievement(null);
    achievementForm.resetFields();
    setAchievementModalVisible(true);
  };

  const handleEditAchievement = (achievement: Achievement) => {
    setEditingAchievement(achievement);
    achievementForm.setFieldsValue(achievement);
    setAchievementModalVisible(true);
  };

  const handleSaveAchievement = async () => {
    try {
      const values = await achievementForm.validateFields();
      if (editingAchievement) {
        await axios.put(`/api/admin/content/achievements/${editingAchievement.id}`, { ...values, id: editingAchievement.id });
        message.success('Conquista atualizada com sucesso');
      } else {
        await axios.post('/api/admin/content/achievements', values);
        message.success('Conquista criada com sucesso');
      }
      setAchievementModalVisible(false);
      loadAchievements();
    } catch (error) {
      message.error('Erro ao salvar conquista');
      console.error('Error saving achievement:', error);
    }
  };

  const handleDeleteAchievement = async (achievementId: number) => {
    try {
      await axios.delete(`/api/admin/content/achievements/${achievementId}`);
      message.success('Conquista excluída com sucesso');
      loadAchievements();
    } catch (error) {
      message.error('Erro ao excluir conquista');
      console.error('Error deleting achievement:', error);
    }
  };

  const handleApproveQuestion = async (questionId: number) => {
    try {
      await axios.put(`/api/admin/content/questions/${questionId}/approve`);
      message.success('Questão aprovada com sucesso');
      loadPendingQuestions();
    } catch (error) {
      message.error('Erro ao aprovar questão');
      console.error('Error approving question:', error);
    }
  };

  const handleRejectQuestion = async (questionId: number) => {
    try {
      await axios.delete(`/api/admin/content/questions/${questionId}`);
      message.success('Questão rejeitada com sucesso');
      loadPendingQuestions();
    } catch (error) {
      message.error('Erro ao rejeitar questão');
      console.error('Error rejecting question:', error);
    }
  };

  const topicColumns = [
    {
      title: 'Nome',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Descrição',
      dataIndex: 'description',
      key: 'description',
    },
    {
      title: 'Ações',
      key: 'actions',
      render: (record: Topic) => (
        <Space>
          <Button type="link" icon={<EditOutlined />} onClick={() => handleEditTopic(record)}>
            Editar
          </Button>
          <Popconfirm
            title="Tem certeza que deseja excluir este tópico?"
            onConfirm={() => handleDeleteTopic(record.id)}
            okText="Sim"
            cancelText="Não"
          >
            <Button type="link" danger icon={<DeleteOutlined />}>
              Excluir
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  const achievementColumns = [
    {
      title: 'Nome',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Descrição',
      dataIndex: 'description',
      key: 'description',
    },
    {
      title: 'Ícone',
      dataIndex: 'icon',
      key: 'icon',
    },
    {
      title: 'Pontos Necessários',
      dataIndex: 'requiredPoints',
      key: 'requiredPoints',
    },
    {
      title: 'Ações',
      key: 'actions',
      render: (record: Achievement) => (
        <Space>
          <Button type="link" icon={<EditOutlined />} onClick={() => handleEditAchievement(record)}>
            Editar
          </Button>
          <Popconfirm
            title="Tem certeza que deseja excluir esta conquista?"
            onConfirm={() => handleDeleteAchievement(record.id)}
            okText="Sim"
            cancelText="Não"
          >
            <Button type="link" danger icon={<DeleteOutlined />}>
              Excluir
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  const questionColumns = [
    {
      title: 'Questão',
      dataIndex: 'questionText',
      key: 'questionText',
    },
    {
      title: 'Dificuldade',
      dataIndex: 'difficulty',
      key: 'difficulty',
    },
    {
      title: 'Tópico',
      key: 'topic',
      render: (record: Question) => record.topic?.name || 'N/A',
    },
    {
      title: 'Ações',
      key: 'actions',
      render: (record: Question) => (
        <Space>
          <Button type="link" icon={<CheckOutlined />} onClick={() => handleApproveQuestion(record.id)}>
            Aprovar
          </Button>
          <Popconfirm
            title="Tem certeza que deseja rejeitar esta questão?"
            onConfirm={() => handleRejectQuestion(record.id)}
            okText="Sim"
            cancelText="Não"
          >
            <Button type="link" danger icon={<CloseOutlined />}>
              Rejeitar
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <div style={{ padding: '24px' }}>
      <Title level={2}>Gestão de Conteúdo</Title>

      <Tabs defaultActiveKey="topics" style={{ marginTop: '24px' }}>
        <TabPane tab="Tópicos" key="topics">
          <Button type="primary" icon={<PlusOutlined />} onClick={handleCreateTopic} style={{ marginBottom: '16px' }}>
            Novo Tópico
          </Button>
          <Table columns={topicColumns} dataSource={topics} rowKey="id" loading={loading} />
        </TabPane>

        <TabPane tab="Conquistas" key="achievements">
          <Button type="primary" icon={<PlusOutlined />} onClick={handleCreateAchievement} style={{ marginBottom: '16px' }}>
            Nova Conquista
          </Button>
          <Table columns={achievementColumns} dataSource={achievements} rowKey="id" loading={loading} />
        </TabPane>

        <TabPane tab="Moderação de Questões" key="questions">
          <Table columns={questionColumns} dataSource={pendingQuestions} rowKey="id" loading={loading} />
        </TabPane>
      </Tabs>

      <Modal
        title={editingTopic ? 'Editar Tópico' : 'Novo Tópico'}
        open={topicModalVisible}
        onOk={handleSaveTopic}
        onCancel={() => setTopicModalVisible(false)}
        okText="Salvar"
        cancelText="Cancelar"
      >
        <Form form={topicForm} layout="vertical">
          <Form.Item name="name" label="Nome" rules={[{ required: true, message: 'Digite o nome do tópico' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="description" label="Descrição" rules={[{ required: true, message: 'Digite a descrição do tópico' }]}>
            <TextArea rows={4} />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title={editingAchievement ? 'Editar Conquista' : 'Nova Conquista'}
        open={achievementModalVisible}
        onOk={handleSaveAchievement}
        onCancel={() => setAchievementModalVisible(false)}
        okText="Salvar"
        cancelText="Cancelar"
      >
        <Form form={achievementForm} layout="vertical">
          <Form.Item name="name" label="Nome" rules={[{ required: true, message: 'Digite o nome da conquista' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="description" label="Descrição" rules={[{ required: true, message: 'Digite a descrição da conquista' }]}>
            <TextArea rows={4} />
          </Form.Item>
          <Form.Item name="icon" label="Ícone" rules={[{ required: true, message: 'Digite o ícone da conquista' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="requiredPoints" label="Pontos Necessários" rules={[{ required: true, message: 'Digite os pontos necessários' }]}>
            <InputNumber min={0} style={{ width: '100%' }} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default AdminContent;
