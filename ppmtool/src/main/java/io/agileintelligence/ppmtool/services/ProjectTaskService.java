package io.agileintelligence.ppmtool.services;

import io.agileintelligence.ppmtool.Repositories.BacklogRepository;
import io.agileintelligence.ppmtool.Repositories.ProjectTaskRepository;
import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.domain.ProjectTask;
import io.agileintelligence.ppmtool.exceptions.ProjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {
    @Autowired
    private BacklogRepository backlogRepository;
    @Autowired
    private ProjectTaskRepository projectTaskRepository;
    @Autowired
    private ProjectService projectService;
    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username)
    {

            //PTs to be added to a specific project, project is not null
            Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();
                    //backlogRepository.findByProjectIdentifier(projectIdentifier);

            //Set the backlog to pt
            projectTask.setBacklog(backlog);
            // IDPRO-1, IDPRO-2  we want our project seq to be structured like this
            Integer BacklogSequence = backlog.getPTSequence();
            // Update the BL Sequence
            BacklogSequence++;
            backlog.setPTSequence(BacklogSequence);
            //Add Sequence to PT
            projectTask.setProjectSequence(projectIdentifier + "-" + BacklogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);
            // Initial Priority  when priority is null
            if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
                projectTask.setPriority(3);
            }
            // Initial status when status is null
            if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
                projectTask.setStatus("TO_DO");
            }

            return projectTaskRepository.save(projectTask);


    }


    public Iterable<ProjectTask> findBacklogById(String backlog_id, String username) {

        projectService.findProjectByIdentifier(backlog_id, username);

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlog_id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String username)
    {
        //make sure we are searching on the right backlog
       projectService.findProjectByIdentifier(backlog_id, username);
        //make sure that our task exist
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
        if(projectTask==null)
        {
            throw new ProjectNotFoundException("Project Task " + pt_id+ " not found");
        }
        // make sure that the backlog/project id in the path corresponds to the right project
        if(!projectTask.getProjectIdentifier().equals(backlog_id))
        {
            throw new ProjectNotFoundException("Project Task "+pt_id+ " doesn't exist in project " + backlog_id);
        }
        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id, String username)
    {
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
        projectTask = updatedTask;
        return projectTaskRepository.save(projectTask);
    }

    public void deletePTbyProjectSequence(String backlog_id, String pt_id, String username)
    {
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
        projectTaskRepository.delete(projectTask);
    }

}
