package org.bbop.apollo.gwt.client;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import org.bbop.apollo.gwt.client.demo.DataGenerator;

import java.util.Comparator;
import java.util.List;

/**
 * Created by ndunn on 12/17/14.
 */
public class UserBrowserPanel extends Composite {
    interface UserBrowserPanelUiBinder extends UiBinder<Widget, UserBrowserPanel> {
    }

    private static UserBrowserPanelUiBinder ourUiBinder = GWT.create(UserBrowserPanelUiBinder.class);
    @UiField
    HTML userGroupHTML;
    @UiField
    HTML name;
    @UiField
    HTML email;
    @UiField
    DataGrid<UserInfo> dataGrid;

    public UserBrowserPanel() {
        initWidget(ourUiBinder.createAndBindUi(this));

        name.setHTML("Bob Jones");
        email.setHTML("bob@jones.gov");
        userGroupHTML.setHTML("<div class='label label-default'>UserGroup1.3</div>");


        TextColumn<UserInfo> firstNameColumn = new TextColumn<UserInfo>() {
            @Override
            public String getValue(UserInfo employee) {
                return employee.getName();
            }
        };
        firstNameColumn.setSortable(true);

//        Column<UserInfo,Number> secondNameColumn = new Column<UserInfo,Number>(new NumberCell()) {
//            @Override
//            public Integer getValue(UserInfo object) {
//                return object.getNumberUserGroups();
//            }
//        };
//        secondNameColumn.setSortable(true);

        SafeHtmlRenderer<String> anchorRenderer = new AbstractSafeHtmlRenderer<String>() {
            @Override
            public SafeHtml render(String object) {
                SafeHtmlBuilder sb = new SafeHtmlBuilder();
                sb.appendHtmlConstant("<a href=\"javascript:;\">").appendEscaped(object)
                        .appendHtmlConstant("</a>");
                return sb.toSafeHtml();
            }
        };

        Column<UserInfo,String> thirdNameColumn = new Column<UserInfo, String>(new ClickableTextCell(anchorRenderer)) {
            @Override
            public String getValue(UserInfo employee) {
                return employee.getEmail();
            }
        };
        thirdNameColumn.setSortable(true);

        dataGrid.addColumn(firstNameColumn, "Name");
        dataGrid.addColumn(thirdNameColumn, "Email");
//        dataGrid.addColumn(thirdNameColumn,"Action");


        ListDataProvider<UserInfo> dataProvider = new ListDataProvider<>();
        dataProvider.addDataDisplay(dataGrid);

        List<UserInfo> trackInfoList = dataProvider.getList();

        for(String user : DataGenerator.getUsers()){
            trackInfoList.add(new UserInfo(user));
        }

        ColumnSortEvent.ListHandler<UserInfo> sortHandler = new ColumnSortEvent.ListHandler<UserInfo>(trackInfoList);
        dataGrid.addColumnSortHandler(sortHandler);
        sortHandler.setComparator(firstNameColumn, new Comparator<UserInfo>() {
            @Override
            public int compare(UserInfo o1, UserInfo o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        sortHandler.setComparator(thirdNameColumn, new Comparator<UserInfo>() {
            @Override
            public int compare(UserInfo o1, UserInfo o2) {
                return o1.getEmail().compareTo(o2.getEmail());
            }
        });
    }
}