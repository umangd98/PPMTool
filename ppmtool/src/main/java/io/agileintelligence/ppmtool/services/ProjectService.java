package io.agileintelligence.ppmtool.services;

import io.agileintelligence.ppmtool.Repositories.ProjectRepository;
import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.exceptions.ProjectIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public Project saveOrUpdateProject(Project project)
    {
        //Logic
        try{
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            return projectRepository.save(project);
        }
        catch (Exception e)
        {
            throw new ProjectIdException("Project Id " + project.getProjectIdentifier().toUpperCase() + "already exists");
        }
    }

    public Project findProjectByIdentifier(String projectId)
    {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if(project==null)
            throw new ProjectIdException("Project does not exists");

        return project;
    }

    public Iterable<Project> findAllProjects()
    {
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String projectid)
    {
        Project project = projectRepository.findByProjectIdentifier(projectid.toUpperCase());
        if(project == null)
        {
            throw new ProjectIdException("Cannot delete Project with ID "+projectid);
        }
        projectRepository.delete(project);
    }



}
