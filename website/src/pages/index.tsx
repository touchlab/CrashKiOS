import React from 'react';
import Layout from '@theme/Layout';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import Process from '@site/src/components/Process';
import HeroHome from '@site/src/components/HeroHome';
import Newsletter from '@site/src/components/Newsletter';


export default function Home(): JSX.Element {
  const {siteConfig} = useDocusaurusContext();
  return (
    <Layout
      title={`Hello from ${siteConfig.title}`}
      description="Description will go into a meta tag in <head />">
      <HeroHome />
      <Process/>
      <Newsletter/>
      <div>&nbsp;</div>
      <div>&nbsp;</div>
    </Layout>
  );
}
