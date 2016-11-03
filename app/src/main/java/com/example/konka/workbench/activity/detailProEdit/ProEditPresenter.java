package com.example.konka.workbench.activity.detailProEdit;

import com.example.konka.workbench.domain.Version;

/**
 * Created by xiaotao on 2016-10-18.
 *
 */
public class ProEditPresenter implements OnProEditListener {
    private IDetailProEditModel detailProEditModel;
    private IDetailProEditView detailProEditView;

    public ProEditPresenter(IDetailProEditView detailProEditView) {
        this.detailProEditView = detailProEditView;
        detailProEditModel = new DetailProEditModel();
    }

    public void updateProject() {
        detailProEditModel.update(detailProEditView.getProject(), this);
    }

    @Override
    public void updateProjectSuccess() {
        /*版本名不同，则向version表插入新的数据*/
        if (!detailProEditView.getNewVersionName().equalsIgnoreCase(detailProEditView.getLastVersion().getVersionName())) {
            Version newVersion = new Version();
            newVersion.setVersionName(detailProEditView.getNewVersionName());
            newVersion.setState(detailProEditView.getState());
            newVersion.setFromProject(detailProEditView.getProject());
            detailProEditModel.addNewVersion(newVersion, this);
            /*版本名相同，但软件状态不同*/
        } else if (!detailProEditView.getState().equals(detailProEditView.getLastVersion().getState())) {
            Version newState = new Version();
            newState.setObjectId(detailProEditView.getLastVersion().getObjectId());
            newState.setState(detailProEditView.getState());
            detailProEditModel.updateVesrion(newState, this);
        }else{
            detailProEditView.updateProjectSuccess();
        }
    }

    @Override
    public void addNewVersionSuccess() {
        detailProEditView.updateProjectSuccess();
    }

    @Override
    public void updateVersionSuccess() {
        detailProEditView.updateProjectSuccess();
    }

    @Override
    public void noNetwork() {
        detailProEditView.showNoNetwork();
    }
}
