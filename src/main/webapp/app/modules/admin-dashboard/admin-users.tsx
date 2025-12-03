import React, { useEffect, useState } from 'react';
import { Table, Button, Modal, Form, Input, Select, Switch, Space, Typography, message, Popconfirm, Tag } from 'antd';
import { EditOutlined, DeleteOutlined, CheckCircleOutlined, CloseCircleOutlined } from '@ant-design/icons';
import axios from 'axios';

const { Title } = Typography;
const { Option } = Select;

interface User {
  id: number;
  login: string;
  firstName: string;
  lastName: string;
  email: string;
  activated: boolean;
  authorities: string[];
}

export const AdminUsers = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(false);
  const [editModalVisible, setEditModalVisible] = useState(false);
  const [selectedUser, setSelectedUser] = useState<User | null>(null);
  const [form] = Form.useForm();

  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    setLoading(true);
    try {
      const response = await axios.get<User[]>('/api/admin/users/all');
      setUsers(response.data);
    } catch (error) {
      message.error('Erro ao carregar usuários');
      console.error('Error loading users:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleEditRoles = (user: User) => {
    setSelectedUser(user);
    form.setFieldsValue({ authorities: user.authorities });
    setEditModalVisible(true);
  };

  const handleUpdateRoles = async () => {
    if (!selectedUser) return;

    try {
      const values = await form.validateFields();
      await axios.put(`/api/admin/users/${selectedUser.id}/roles`, values.authorities);
      message.success('Roles atualizadas com sucesso');
      setEditModalVisible(false);
      loadUsers();
    } catch (error) {
      message.error('Erro ao atualizar roles');
      console.error('Error updating roles:', error);
    }
  };

  const handleToggleActivation = async (user: User) => {
    try {
      if (user.activated) {
        await axios.put(`/api/admin/users/${user.id}/deactivate`);
        message.success('Usuário desativado com sucesso');
      } else {
        await axios.put(`/api/admin/users/${user.id}/activate`);
        message.success('Usuário ativado com sucesso');
      }
      loadUsers();
    } catch (error) {
      message.error('Erro ao alterar status do usuário');
      console.error('Error toggling activation:', error);
    }
  };

  const handleDeleteUser = async (userId: number) => {
    try {
      await axios.delete(`/api/admin/users/${userId}`);
      message.success('Usuário excluído com sucesso');
      loadUsers();
    } catch (error) {
      message.error('Erro ao excluir usuário');
      console.error('Error deleting user:', error);
    }
  };

  const columns = [
    {
      title: 'Login',
      dataIndex: 'login',
      key: 'login',
    },
    {
      title: 'Nome',
      key: 'name',
      render: (record: User) => `${record.firstName} ${record.lastName}`,
    },
    {
      title: 'Email',
      dataIndex: 'email',
      key: 'email',
    },
    {
      title: 'Status',
      key: 'activated',
      render: (record: User) => <Tag color={record.activated ? 'green' : 'red'}>{record.activated ? 'Ativo' : 'Inativo'}</Tag>,
    },
    {
      title: 'Roles',
      key: 'authorities',
      render: (record: User) => (
        <>
          {record.authorities.map(auth => (
            <Tag key={auth} color="blue">
              {auth.replace('ROLE_', '')}
            </Tag>
          ))}
        </>
      ),
    },
    {
      title: 'Ações',
      key: 'actions',
      render: (record: User) => (
        <Space>
          <Button type="link" icon={<EditOutlined />} onClick={() => handleEditRoles(record)}>
            Editar Roles
          </Button>
          <Button
            type="link"
            icon={record.activated ? <CloseCircleOutlined /> : <CheckCircleOutlined />}
            onClick={() => handleToggleActivation(record)}
          >
            {record.activated ? 'Desativar' : 'Ativar'}
          </Button>
          {record.login !== 'admin' && (
            <Popconfirm
              title="Tem certeza que deseja excluir este usuário?"
              onConfirm={() => handleDeleteUser(record.id)}
              okText="Sim"
              cancelText="Não"
            >
              <Button type="link" danger icon={<DeleteOutlined />}>
                Excluir
              </Button>
            </Popconfirm>
          )}
        </Space>
      ),
    },
  ];

  return (
    <div style={{ padding: '24px' }}>
      <Title level={2}>Gestão de Usuários</Title>

      <Table columns={columns} dataSource={users} rowKey="id" loading={loading} style={{ marginTop: '24px' }} />

      <Modal
        title="Editar Roles do Usuário"
        open={editModalVisible}
        onOk={handleUpdateRoles}
        onCancel={() => setEditModalVisible(false)}
        okText="Salvar"
        cancelText="Cancelar"
      >
        <Form form={form} layout="vertical">
          <Form.Item name="authorities" label="Roles" rules={[{ required: true, message: 'Selecione pelo menos uma role' }]}>
            <Select mode="multiple" placeholder="Selecione as roles">
              <Option value="ROLE_ADMIN">Admin</Option>
              <Option value="ROLE_TEACHER">Professor</Option>
              <Option value="ROLE_STUDENT">Aluno</Option>
            </Select>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default AdminUsers;
