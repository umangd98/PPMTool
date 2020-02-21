package io.agileintelligence.ppmtool.services;

import io.agileintelligence.ppmtool.Repositories.BacklogRepository;
import io.agileintelligence.ppmtool.Repositories.ProjectRepository;
import io.agileintelligence.ppmtool.Repositories.UserRepository;
import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.domain.User;
import io.agileintelligence.ppmtool.exceptions.ProjectIdException;
import io.agileintelligence.ppmtool.exceptions.ProjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private BacklogRepository backlogRepository;
    @Autowired
    private UserRepository userRepository;
    public Project saveOrUpdateProject(Project project, String username)
    {
        if(project.getId()!=null)
        {
            Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
            if(existingProject!=null && (!existingProject.getProjectLeader().equals(username)))
            {
                throw new ProjectNotFoundException("Project Not found in your account");
            }
            else if(existingProject == null)
            {
                throw new ProjectNotFoundException("Project with ID: "+ project.getProjectIdentifier() + " doesn't exist.");
            }


        }
        //Logic
        try{

            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            if(project.getId()==null)       //create
            {
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }
            if(project.getId()!=null)   // update
            {
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }

            return projectRepository.save(project);
        }
        catch (Exception e)
        {
            throw new ProjectIdException("Project Id " + project.getProjectIdentifier().toUpperCase() + "already exists");
        }
    }

    public Project findProjectByIdentifier(String projectId, String username)
    {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if(project==null)
            throw new ProjectIdException("Project does not exists");
        if(!project.getProjectLeader().equals(username))
            throw new ProjectNotFoundException(("Project Not found in your account"));
        return project;
    }

    public Iterable<Project> findAllProjects(String username)
    {
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier(String projectid, String username)
    {
        projectRepository.delete(findProjectByIdentifier(projectid, username));
    }



}
