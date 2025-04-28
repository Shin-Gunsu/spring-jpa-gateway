const express = require('express');
const router = express.Router();
const milestoneController = require('../controllers/milestoneController');

// Milestone routes
router.get('/:projectId/milestones', milestoneController.getAllMilestones);
router.get('/:projectId/milestones/:milestoneId', milestoneController.getMilestone);
router.get('/:projectId/milestones/:milestoneId/tasks', milestoneController.getMilestoneTasks);
router.post('/:projectId/milestones/:milestoneId/delete', milestoneController.deleteMilestone);
router.post('/:projectId/milestones', milestoneController.createMilestone);
router.post('/:projectId/milestones/:milestoneId/tasks', milestoneController.addTaskToMilestone);
router.post('/:projectId/milestones/:milestoneId/tasks/:taskId/delete', milestoneController.removeTaskFromMilestone);

module.exports = router;