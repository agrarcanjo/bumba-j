export const triggerAchievementNotification = (achievement: { id: number; name: string; description: string; icon: string }) => {
  const event = new CustomEvent('achievement-unlocked', {
    detail: achievement,
  });
  window.dispatchEvent(event);
};
