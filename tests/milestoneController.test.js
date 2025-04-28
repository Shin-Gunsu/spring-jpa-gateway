const milestoneController = require('../controllers/milestoneController');
const taskController = require('../controllers/taskController');

// Mock request and response objects
const mockRequest = (params = {}, body = {}, headers = {}) => ({
  params,
  body,
  headers
});

const mockResponse = () => {
  const res = {};
  res.status = jest.fn().mockReturnValue(res);
  res.json = jest.fn().mockReturnValue(res);
  res.send = jest.fn().mockReturnValue(res);
  return res;
};

describe('Milestone Controller Tests', () => {
  beforeEach(() => {
    // Reset the database before each test
    milestoneController._resetDatabase();
    taskController._resetDatabase();
  });

  test('createMilestone should create a milestone and return the correct response', () => {
    const req = mockRequest(
      { projectId: 'project1' }, 
      { 
        name: 'Test Milestone',
        start_date: '2025-01-01',
        end_date: '2025-02-01'
      }
    );
    const res = mockResponse();

    milestoneController.createMilestone(req, res);

    expect(res.status).toHaveBeenCalledWith(201);
    expect(res.json).toHaveBeenCalledWith(
      expect.objectContaining({
        id: 1,
        name: 'Test Milestone',
        start_date: '2025-01-01',
        end_date: '2025-02-01'
      })
    );
  });

  test('getAllMilestones should return all milestones for a project', () => {
    // Create some milestones first
    const req1 = mockRequest(
      { projectId: 'project1' }, 
      { name: 'Milestone 1' }
    );
    const req2 = mockRequest(
      { projectId: 'project1' }, 
      { name: 'Milestone 2' }
    );
    const req3 = mockRequest(
      { projectId: 'project2' }, 
      { name: 'Milestone 3' }
    );
    
    const res = mockResponse();
    milestoneController.createMilestone(req1, res);
    milestoneController.createMilestone(req2, res);
    milestoneController.createMilestone(req3, res);

    // Now get all milestones for project1
    const getReq = mockRequest({ projectId: 'project1' });
    const getRes = mockResponse();

    milestoneController.getAllMilestones(getReq, getRes);

    expect(getRes.status).toHaveBeenCalledWith(200);
    expect(getRes.json).toHaveBeenCalledWith(
      expect.arrayContaining([
        expect.objectContaining({
          id: 1,
          name: 'Milestone 1'
        }),
        expect.objectContaining({
          id: 2,
          name: 'Milestone 2'
        })
      ])
    );
    // Make sure Milestone 3 is not returned
    expect(getRes.json).not.toHaveBeenCalledWith(
      expect.arrayContaining([
        expect.objectContaining({
          id: 3,
          name: 'Milestone 3'
        })
      ])
    );
  });

  test('addTaskToMilestone and getMilestoneTasks should work correctly', () => {
    // Create a milestone first
    const createMilestoneReq = mockRequest(
      { projectId: 'project1' }, 
      { name: 'Test Milestone' }
    );
    const createMilestoneRes = mockResponse();
    milestoneController.createMilestone(createMilestoneReq, createMilestoneRes);

    // Create tasks
    const createTask1Req = mockRequest(
      { projectId: 'project1' }, 
      { name: 'Task 1' },
      { 'x-member-id': 'member1' }
    );
    const createTask2Req = mockRequest(
      { projectId: 'project1' }, 
      { name: 'Task 2' },
      { 'x-member-id': 'member1' }
    );
    const createTaskRes = mockResponse();
    taskController.createTask(createTask1Req, createTaskRes);
    taskController.createTask(createTask2Req, createTaskRes);

    // Add first task to milestone
    const addTaskReq = mockRequest(
      { projectId: 'project1', milestoneId: '1' }, 
      { task_id: 1 }
    );
    const addTaskRes = mockResponse();
    
    milestoneController.addTaskToMilestone(addTaskReq, addTaskRes);

    expect(addTaskRes.status).toHaveBeenCalledWith(204);

    // Get milestone tasks
    const getTasksReq = mockRequest({ projectId: 'project1', milestoneId: '1' });
    const getTasksRes = mockResponse();

    milestoneController.getMilestoneTasks(getTasksReq, getTasksRes);

    expect(getTasksRes.status).toHaveBeenCalledWith(200);
    expect(getTasksRes.json).toHaveBeenCalledWith(
      expect.arrayContaining([
        expect.objectContaining({
          id: 1,
          name: 'Task 1'
        })
      ])
    );
    // Task 2 should not be included
    expect(getTasksRes.json).not.toHaveBeenCalledWith(
      expect.arrayContaining([
        expect.objectContaining({
          id: 2,
          name: 'Task 2'
        })
      ])
    );
  });

  test('removeTaskFromMilestone should remove a task from a milestone', () => {
    // Create a milestone
    const createMilestoneReq = mockRequest(
      { projectId: 'project1' }, 
      { name: 'Test Milestone' }
    );
    const createMilestoneRes = mockResponse();
    milestoneController.createMilestone(createMilestoneReq, createMilestoneRes);

    // Create a task
    const createTaskReq = mockRequest(
      { projectId: 'project1' }, 
      { name: 'Test Task' },
      { 'x-member-id': 'member1' }
    );
    const createTaskRes = mockResponse();
    taskController.createTask(createTaskReq, createTaskRes);

    // Add task to milestone
    const addTaskReq = mockRequest(
      { projectId: 'project1', milestoneId: '1' }, 
      { task_id: 1 }
    );
    const addTaskRes = mockResponse();
    milestoneController.addTaskToMilestone(addTaskReq, addTaskRes);

    // Remove task from milestone
    const removeTaskReq = mockRequest({ 
      projectId: 'project1', 
      milestoneId: '1',
      taskId: '1'
    });
    const removeTaskRes = mockResponse();

    milestoneController.removeTaskFromMilestone(removeTaskReq, removeTaskRes);

    expect(removeTaskRes.status).toHaveBeenCalledWith(204);

    // Verify task is removed by getting milestone tasks
    const getTasksReq = mockRequest({ projectId: 'project1', milestoneId: '1' });
    const getTasksRes = mockResponse();

    milestoneController.getMilestoneTasks(getTasksReq, getTasksRes);

    expect(getTasksRes.status).toHaveBeenCalledWith(200);
    expect(getTasksRes.json).toHaveBeenCalledWith([]);
  });

  test('deleteMilestone should remove a milestone', () => {
    // Create a milestone
    const createMilestoneReq = mockRequest(
      { projectId: 'project1' }, 
      { name: 'Test Milestone' }
    );
    const createMilestoneRes = mockResponse();
    milestoneController.createMilestone(createMilestoneReq, createMilestoneRes);

    // Delete the milestone
    const deleteMilestoneReq = mockRequest({ 
      projectId: 'project1', 
      milestoneId: '1'
    });
    const deleteMilestoneRes = mockResponse();

    milestoneController.deleteMilestone(deleteMilestoneReq, deleteMilestoneRes);

    expect(deleteMilestoneRes.status).toHaveBeenCalledWith(204);

    // Verify milestone is deleted by trying to get it
    const getMilestoneReq = mockRequest({ projectId: 'project1', milestoneId: '1' });
    const getMilestoneRes = mockResponse();

    milestoneController.getMilestone(getMilestoneReq, getMilestoneRes);

    expect(getMilestoneRes.status).toHaveBeenCalledWith(404);
  });
});