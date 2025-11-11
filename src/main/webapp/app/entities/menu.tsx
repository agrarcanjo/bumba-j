import React from 'react';
import { Translate } from 'react-jhipster'; // eslint-disable-line

import MenuItem from 'app/shared/layout/menus/menu-item'; // eslint-disable-line

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/user-profile">
        <Translate contentKey="global.menu.entities.userProfile" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/topic">
        <Translate contentKey="global.menu.entities.topic" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/question">
        <Translate contentKey="global.menu.entities.question" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/lesson">
        <Translate contentKey="global.menu.entities.lesson" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/lesson-question">
        <Translate contentKey="global.menu.entities.lessonQuestion" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/class-room">
        <Translate contentKey="global.menu.entities.classRoom" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/class-member">
        <Translate contentKey="global.menu.entities.classMember" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/lesson-assignment">
        <Translate contentKey="global.menu.entities.lessonAssignment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/student-lesson-progress">
        <Translate contentKey="global.menu.entities.studentLessonProgress" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/attempt">
        <Translate contentKey="global.menu.entities.attempt" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/achievement">
        <Translate contentKey="global.menu.entities.achievement" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-achievement">
        <Translate contentKey="global.menu.entities.userAchievement" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/recommendation">
        <Translate contentKey="global.menu.entities.recommendation" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
